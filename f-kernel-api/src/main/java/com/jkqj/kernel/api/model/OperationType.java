package com.jkqj.kernel.api.model;

import java.io.Serializable;
import java.util.Arrays;

public enum OperationType implements Serializable {
    Undefined(0),
    Insert(1),
    Update(2),
    Delete(3);

    private final int code;

    OperationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OperationType of(int code) {
        return Arrays
                .stream(OperationType.values())
                .filter(type -> type.getCode() == code)
                .findFirst()
                .orElse(Undefined);
    }
}
