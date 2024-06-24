package com.sfs.image.mgmt.kakfa;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

    @Value("${kafka.topic.user-image}")
    private String topic;

    /**
     * Consumes messages from the Kafka topic.
     * This method listens for messages on the specified Kafka topic and processes them.
     * 
     * @param record the ConsumerRecord containing the message and metadata.
     * @param acknowledgment the Acknowledgment object for manual offset management.
     */
    @KafkaListener(topics = "${kafka.topic.user-image}", groupId = "${kafka.consumer.group-id}")
    public void consumeMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
        	log.info("record message: {}", record);
            String message = record.value();
            log.info("Consumed message: {}", message);
            acknowledgment.acknowledge();

            stopWatch.stop();
            log.info("Time taken to process message: {} ms", stopWatch.getTotalTimeMillis());
        } catch (Exception ex) {
            stopWatch.stop();
            log.error("Error processing message from topic {}: {} in {} ms", topic, record.value(), stopWatch.getTotalTimeMillis(), ex);
           
        }
    }
}
