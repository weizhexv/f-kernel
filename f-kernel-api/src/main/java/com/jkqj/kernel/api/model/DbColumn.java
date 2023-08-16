package com.jkqj.kernel.api.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DbColumn implements Serializable {
    private Class<?> type;

    private String name;

    private Object value;

    private Boolean updated;
}
