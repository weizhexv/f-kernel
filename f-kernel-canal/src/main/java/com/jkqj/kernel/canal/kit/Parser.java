package com.jkqj.kernel.canal.kit;

import com.jkqj.common.utils.MyLocalDateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public final class Parser {

    public static String parseSubscribes(List<String> subscribes) {
        if (CollectionUtils.isEmpty(subscribes)) {
            return StringUtils.EMPTY;
        }
        return subscribes.stream().map(StringUtils::trim)
                .collect(Collectors.joining(","));
    }

    public static Class<?> parseType(int sqlType) {
        switch (sqlType) {
            case Types.BIT:
            case Types.BOOLEAN:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return Integer.class;
            case Types.BIGINT:
                return Long.class;
            case Types.DECIMAL:
            case Types.NUMERIC:
                return BigDecimal.class;
            case Types.REAL:
                return Float.class;
            case Types.FLOAT:
            case Types.DOUBLE:
                return Double.class;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
                return String.class;
            case Types.DATE:
                return LocalDate.class;
            case Types.TIME:
                return LocalTime.class;
            case Types.TIMESTAMP:
                return LocalDateTime.class;
            default:
                log.warn("undefined java sql type:{}", sqlType);
                return String.class;
        }
    }

    public static Object parseValue(Class<?> type, String value) {
        Object obj = null;

        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            if (type == Integer.class) {
                obj = Integer.parseInt(value);
            } else if (type == Long.class) {
                obj = Long.parseLong(value);
            } else if (type == BigDecimal.class) {
                obj = new BigDecimal(value);
            } else if (type == Float.class) {
                obj = Float.parseFloat(value);
            } else if (type == Double.class) {
                obj = Double.parseDouble(value);
            } else if (type == String.class) {
                obj = value;
            } else if (type == LocalDate.class) {
                obj = LocalDate.parse(value);
            } else if (type == LocalTime.class) {
                obj = LocalTime.parse(value);
            } else if (type == LocalDateTime.class) {
                if (StringUtils.contains(value, '.')) {
                    obj = MyLocalDateTimeUtils.milliTimeFormat(value);
                } else {
                    obj = MyLocalDateTimeUtils.timeFormat(value);
                }
            }
        } catch (Exception e) {
            log.error("parse value error, type {} value {}", type, value, e);
            obj = value;
        }

        return obj;
    }
}
