package edu.java.bot.configuration.kafka.props;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerProperties {
    private String bootstrapServers;
    private String clientId;
    private String acksMode;
    private Duration deliveryTimeout;
    private Integer lingerMs;
    private Integer batchSize;
    private Integer maxInFlightPerConnection;
    private Boolean enableIdempotence;
    private String topic;
    private String securityProtocol;
    private String saslMechanism;
    private String saslJaasConfig;
}
