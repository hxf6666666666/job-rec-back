package org.example.jobrecback.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("xf.config")
public class XFConfig {
    private String appId;

    private String apiSecret;

    private String apiKey;

    private String hostUrl;

    private Integer maxResponseTime;
}
