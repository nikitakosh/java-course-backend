package edu.java.scrapper.kafka;


import edu.java.configuration.kafka.KafkaProducerConfig;
import edu.java.configuration.kafka.KafkaProducerProperties;
import edu.java.controllers.dto.LinkUpdate;
import static java.lang.Thread.sleep;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@SpringBootTest(classes = {KafkaProducerConfig.class})
@EnableConfigurationProperties(value = KafkaProducerProperties.class)
@Slf4j
public class KafkaProducerTest extends KafkaIntegrationEnvironment {


    @Autowired
    private KafkaProducerProperties properties;

    public ProducerFactory<String, LinkUpdate> getProducerFactory(KafkaProducerProperties kafkaProducerProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerProperties.getClientId());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.getAcksMode());
        props.put(
                ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
                (int) kafkaProducerProperties.getDeliveryTimeout().toMillis()
        );
        props.put(
                ProducerConfig.LINGER_MS_CONFIG,
                kafkaProducerProperties.getLingerMs()
        );
        props.put(
                ProducerConfig.BATCH_SIZE_CONFIG,
                kafkaProducerProperties.getBatchSize()
        );
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    public KafkaTemplate<String, LinkUpdate> getKafkaTemplate(ProducerFactory<String, LinkUpdate> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    public Map<String, Object> getConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bot");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdate.class);
        return props;
    }


    @Test
    public void sendMessageTest() {
        log.info(kafkaContainer.getBootstrapServers());
        KafkaTemplate<String, LinkUpdate> kafkaTemplate = getKafkaTemplate(
                getProducerFactory(properties)
        );
        try (KafkaConsumer<String, LinkUpdate> kafkaConsumer = new KafkaConsumer<>(getConsumerProps())) {
            kafkaConsumer.subscribe(List.of("link-updates"));
            kafkaTemplate.send(
                    "link-updates",
                    new LinkUpdate("https://example.com", "description", List.of(1L), false, "additional info")
            );

            Iterable<ConsumerRecord<String, LinkUpdate>> consumerRecords = kafkaConsumer
                    .poll(Duration.ofSeconds(3))
                    .records("link-updates");
            List<ConsumerRecord<String, LinkUpdate>> result = new ArrayList<>();
            consumerRecords.forEach(result::add);
            Assertions.assertFalse(result.isEmpty());
        }


    }
}