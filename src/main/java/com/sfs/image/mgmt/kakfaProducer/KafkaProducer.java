package com.sfs.image.mgmt.kakfaProducer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	 private final String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${kafka.topic.user-image}")String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic=topic;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
    }
}
