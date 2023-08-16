package com.jkqj.kernel.canal.nats;

import com.jkqj.common.utils.JsonUtils;
import com.jkqj.kernel.api.model.DbEntity;
import com.jkqj.kernel.api.subject.CanalSubject;
import com.jkqj.kernel.canal.kit.Utils;
import com.jkqj.kernel.canal.plugin.Context;
import com.jkqj.kernel.canal.plugin.impl.TracePlugin;
import com.jkqj.nats.MessageClient;
import com.jkqj.nats.Subject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Component
public class NatsClient {

    @Autowired
    private MessageClient messageClient;

    public void publish(Context context) {
        if (context == null) {
            log.warn("skip blank context");
            return;
        }

        List<DbEntity> dbEntities = context.getDbEntities();
        if (CollectionUtils.isEmpty(dbEntities)) {
            log.warn("skip blank dbEntity");
            return;
        }

        dbEntities.parallelStream().forEach(dbEntity -> {
                    String name = Utils.concat(dbEntity.getDatabaseName(), '.', dbEntity.getTableName());

                    publish(new CanalSubject(name), JsonUtils.toJson(dbEntity), dbEntity.getTraceId());
                }
        );
    }

    private void publish(CanalSubject subject, String body, String traceId) {
        log.debug("publish subject {}, body {}, traceId {}", subject, body, traceId);

        Subject.check(subject);
        checkArgument(StringUtils.isNotBlank(body));

        messageClient.publish(
                subject, Collections.singletonMap(TracePlugin.TRACE_ID, traceId), body.getBytes(StandardCharsets.UTF_8));
    }
}
