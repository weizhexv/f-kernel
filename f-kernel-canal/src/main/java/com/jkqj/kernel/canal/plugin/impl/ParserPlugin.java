package com.jkqj.kernel.canal.plugin.impl;

import com.alibaba.otter.canal.protocol.Message;
import com.google.common.collect.Lists;
import com.jkqj.kernel.canal.kit.Parser;
import com.jkqj.kernel.api.model.DbColumn;
import com.jkqj.kernel.api.model.DbColumns;
import com.jkqj.kernel.api.model.DbEntity;
import com.jkqj.kernel.api.model.OperationType;
import com.jkqj.kernel.canal.plugin.AbstractPlugin;
import com.jkqj.kernel.canal.plugin.Context;
import com.jkqj.kernel.canal.plugin.PluginType;
import com.jkqj.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

import static com.alibaba.otter.canal.protocol.CanalEntry.*;
import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Component
public class ParserPlugin extends AbstractPlugin {

    @Override
    public PluginType getType() {
        return PluginType.PARSER;
    }

    @Override
    public void doProcess(Context context) {
        checkArgument(context.getMessage() != null);

        Message message = context.getMessage();

        for (Entry entry : message.getEntries()) {
            if (!entry.getEntryType().equals(EntryType.ROWDATA)) {
                continue;
            }

            parseEntry(context, entry);
        }

        log.debug("context after parse {}", JsonUtils.toJson(context));
    }

    private void parseEntry(Context context, Entry entry) {
        RowChange rowChange = null;

        try {
            rowChange = RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            log.error("parse rowChange failed, entry:{}", entry.toString());

            throw new RuntimeException(e);
        }

        List<DbColumns> dbColumnsList = parseRowChange(rowChange);

        if (CollectionUtils.isNotEmpty(dbColumnsList)) {
            for (DbColumns dbColumns : dbColumnsList) {
                DbEntity dbEntity = new DbEntity();

                dbEntity.setDatabaseName(entry.getHeader().getSchemaName());
                dbEntity.setTableName(entry.getHeader().getTableName());
                dbEntity.setOperationType(OperationType.of(rowChange.getEventType().getNumber()));
                dbEntity.setDbColumns(dbColumns);
                Timestamp timestamp = new Timestamp(entry.getHeader().getExecuteTime());
                dbEntity.setExecutedAt(timestamp.toLocalDateTime());
                dbEntity.setTraceId(context.getTraceId());

                context.getDbEntities().add(dbEntity);
            }
        }
    }

    private List<DbColumns> parseRowChange(RowChange rowChange) {
        List<DbColumns> dbColumnsList = Lists.newArrayList();

        switch (rowChange.getEventType()) {
            case INSERT:
            case UPDATE:
                for (RowData rowData : rowChange.getRowDatasList()) {
                    dbColumnsList.add(buildDbColumns(rowData.getAfterColumnsList()));
                }
                break;
            case DELETE:
                for (RowData rowData : rowChange.getRowDatasList()) {
                    dbColumnsList.add(buildDbColumns(rowData.getBeforeColumnsList()));
                }
                break;
        }

        return dbColumnsList;
    }

    private DbColumns buildDbColumns(List<Column> columns) {
        DbColumns dbColumns = new DbColumns();

        for (Column column : columns) {
            DbColumn dbColumn = new DbColumn();

            Class<?> type = Parser.parseType(column.getSqlType());
            dbColumn.setType(type);
            dbColumn.setName(column.getName());
            dbColumn.setValue(Parser.parseValue(type, column.getValue()));
            dbColumn.setUpdated(column.getUpdated());

            dbColumns.add(dbColumn);
        }

        return dbColumns;
    }

}
