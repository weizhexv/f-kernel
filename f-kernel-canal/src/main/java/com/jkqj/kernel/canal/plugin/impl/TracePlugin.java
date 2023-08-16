package com.jkqj.kernel.canal.plugin.impl;

import com.jkqj.kernel.canal.plugin.AbstractPlugin;
import com.jkqj.kernel.canal.plugin.Context;
import com.jkqj.kernel.canal.plugin.PluginType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class TracePlugin extends AbstractPlugin {
    public static final String TRACE_ID = "trace-id";

    @Override
    public PluginType getType() {
        return PluginType.TRACE;
    }

    @Override
    public void doProcess(Context context) {
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);
        context.setTraceId(traceId);
    }

    @Override
    public void postProcess(Context context) {
        MDC.clear();
    }
}
