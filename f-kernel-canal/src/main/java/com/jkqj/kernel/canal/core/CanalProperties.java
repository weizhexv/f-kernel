package com.jkqj.kernel.canal.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "canal")
public class CanalProperties {
    private String host;
    private Integer port;
    private String destination;
    private String username;
    private String password;
    private List<String> subscribes;
}
