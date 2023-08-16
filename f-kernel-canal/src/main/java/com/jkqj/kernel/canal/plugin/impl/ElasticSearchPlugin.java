package com.jkqj.kernel.canal.plugin.impl;

import com.jkqj.kernel.canal.plugin.AbstractPlugin;
import com.jkqj.kernel.canal.plugin.Context;
import com.jkqj.kernel.canal.plugin.PluginType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElasticSearchPlugin extends AbstractPlugin {

    @Override
    public PluginType getType() {
        return PluginType.ELASTIC_SEARCH;
    }

    @Override
    public void doProcess(Context context) {

    }

}
