package org.example.jobrecback.config;

import com.qingstor.sdk.config.EnvContext;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("qingstor")
public class QingStorConfig {

    private String accessKeyId;
    private String secretAccessKey;
    private String endpoint;
    private boolean enableVirtualHostStyle;
    private boolean cnameSupport;
    private int readTimeout;
    private int connectionTimeout;
    private int writeTimeout;
}
