package com.jkqj.kernel.canal.core;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jkqj.kernel.canal.kit.Parser;
import com.jkqj.kernel.canal.plugin.Context;
import com.jkqj.kernel.canal.plugin.PluginChain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
public class CanalConsumer implements LifeCycle {

    private volatile Boolean start = false;
    private static final int BATCH_SIZE = 1000;

    private final ExecutorService executorService;
    private final CanalConnector canalConnector;
    private final CanalProperties canalProperties;
    private final PluginChain pluginChain;

    public CanalConsumer(CanalConnector canalConnector, CanalProperties canalProperties, PluginChain pluginChain) {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("canal-consumer-%d").build();

        this.executorService = Executors.newSingleThreadExecutor(threadFactory);
        this.canalConnector = canalConnector;
        this.canalProperties = canalProperties;
        this.pluginChain = pluginChain;
    }

    @Override
    public void start() {
        String subscribes = Parser.parseSubscribes(canalProperties.getSubscribes());
        log.info("parse consumer subscribe filter {}", subscribes);

        canalConnector.connect();
        canalConnector.subscribe(subscribes);
        executorService.submit(this::processTask);
        start = true;

        log.info("canal consumer start");
    }

    @Override
    public void stopGracefully() {
        start = false;

        canalConnector.unsubscribe();
        canalConnector.disconnect();

        log.info("canal consumer stop");
    }

    @Override
    public boolean isStarted() {
        return start;
    }

    private void processTask() {
        while (isStarted()) {
            try {
                Message message = canalConnector.getWithoutAck(BATCH_SIZE);

                checkState(message != null);
                log.info("get message id {}, size {}", message.getId(), message.getEntries().size());

                if (CollectionUtils.isNotEmpty(message.getEntries())) {
                    pluginChain.handle(new Context(message));

                    canalConnector.ack(message.getId());
                }

            } catch (Exception e) {
                log.error("consume error {}", ExceptionUtils.getStackTrace(e));
                try {
                    canalConnector.rollback();
                } catch (Exception ignore) {

                }
            }

            try {
                Thread.sleep(100L);

            } catch (InterruptedException ie) {
                log.error("sleep error {}", ExceptionUtils.getStackTrace(ie));
            }
        }
    }

}
