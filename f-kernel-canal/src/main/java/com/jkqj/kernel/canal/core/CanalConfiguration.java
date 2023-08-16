package com.jkqj.kernel.canal.core;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.jkqj.kernel.canal.plugin.Plugin;
import com.jkqj.kernel.canal.plugin.PluginChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.List;

@Configuration
public class CanalConfiguration {

    @Bean
    public CanalConnector canalConnector(CanalProperties properties) {
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(properties.getHost(), properties.getPort());

        return CanalConnectors.newSingleConnector(inetSocketAddress, properties.getDestination(),
                properties.getUsername(), properties.getPassword());
    }

    @Bean
    public PluginChain pluginChain(List<Plugin> plugins) {
        return new PluginChain(plugins);
    }

    @Bean
    public CanalConsumer canalConsumer(CanalConnector canalConnector, CanalProperties canalProperties, PluginChain pluginChain) {
        return new CanalConsumer(canalConnector, canalProperties, pluginChain);
    }

}
