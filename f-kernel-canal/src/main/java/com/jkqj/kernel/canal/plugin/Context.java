package com.jkqj.kernel.canal.plugin;

import com.alibaba.otter.canal.protocol.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jkqj.kernel.api.model.DbEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Context implements Serializable {
    @JsonIgnore
    private Message message;

    private String traceId;

    private List<DbEntity> dbEntities = new ArrayList<>();

    public Context(Message message) {
        this.message = message;
    }
}
