package com.sfs.image.mgmt.kakfaProducer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducer {

	 private final String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${kafka.topic.user-image}")String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic=topic;
    }
	/*
	 * public String sendMessage(String message) { kafkaTemplate.send(topic,
	 * message); return message; }
	 */
    
   
    public String sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

        try {
            SendResult<String, String> sendResult = future.get(); // Waits for the result
            String kafkaStatus = "Message sent to topic " + topic + " with offset " + sendResult.getRecordMetadata().offset();
            log.info(kafkaStatus);
            return kafkaStatus;
        } catch (InterruptedException | ExecutionException ex) {
            String kafkaStatus = "Failed to send message to topic " + topic;
            log.error(kafkaStatus, ex);
            return kafkaStatus;
        }
    
    }
}
