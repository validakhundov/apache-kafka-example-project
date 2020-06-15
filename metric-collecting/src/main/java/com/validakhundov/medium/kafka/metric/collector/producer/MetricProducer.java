package com.validakhundov.medium.kafka.metric.collector.producer;

import com.validakhundov.medium.kafka.metric.collector.model.Metric;
import com.fasterxml.jackson.databind.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;
import lombok.experimental.UtilityClass;
import java.util.Properties;

@UtilityClass
public class MetricProducer {
    public void produceMetric(Metric metric, String token) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.110:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        KafkaProducer producer = new KafkaProducer(properties);
        producer.send(new ProducerRecord<String, JsonNode>("metric_" + token, new ObjectMapper().valueToTree(metric)));
        producer.close();
    }
}