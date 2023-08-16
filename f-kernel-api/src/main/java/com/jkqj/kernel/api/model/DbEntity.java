package com.jkqj.kernel.api.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class DbEntity implements Serializable {
    private String databaseName;

    private String tableName;

    private OperationType operationType;

    private DbColumns dbColumns = new DbColumns();

    private LocalDateTime executedAt;

    private String traceId;

    private transient volatile Map<String, DbColumn> dbColumnsMap = null;

    public DbColumn getDbColumnByName(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name should not blank");
        }

        if (dbColumns.size() == 0) {
            return null;
        }

        if (dbColumnsMap == null) {
            synchronized (this) {
                if (dbColumnsMap == null) {
                    dbColumnsMap = dbColumns.stream().collect(Collectors.toMap(DbColumn::getName, dbColumn -> dbColumn));
                }
            }
        }

        return dbColumnsMap.get(name);
    }
}
