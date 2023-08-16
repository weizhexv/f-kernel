package com.jkqj.kernel.canal.plugin.impl;

import com.jkqj.kernel.api.model.DbEntity;
import com.jkqj.kernel.canal.nats.NatsClient;
import com.jkqj.kernel.canal.plugin.AbstractPlugin;
import com.jkqj.kernel.canal.plugin.Context;
import com.jkqj.kernel.canal.plugin.PluginType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MQPlugin extends AbstractPlugin {

    @Autowired
    private NatsClient natsClient;

    @Override
    public PluginType getType() {
        return PluginType.MQ;
    }

    @Override
    public void doProcess(Context context) {
        List<DbEntity> dbEntities = context.getDbEntities();

        if (CollectionUtils.isEmpty(dbEntities)) {
            log.debug("skip blank dbEntities");
            return;
        }

        natsClient.publish(context);
    }
}
