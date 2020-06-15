package com.validakhundov.medium.kafka.metric.monitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private SimpMessagingTemplate template;

    @KafkaListener(topics = "metric_" + "${metric.collector.token}", groupId = "0")
    public void consumeMetric(String metric) {
        template.convertAndSend("/metric", metric);
    }
}

