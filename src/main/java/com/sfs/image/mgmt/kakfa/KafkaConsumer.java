package com.sfs.image.mgmt.kakfa;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfs.image.mgmt.entity.ProducerMessage;

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
	/*
	 * @KafkaListener(topics = "${kafka.topic.user-image}", groupId =
	 * "${kafka.consumer.group-id}") public void
	 * consumeMessage(ConsumerRecord<String, String> record, Acknowledgment
	 * acknowledgment) { StopWatch stopWatch = new StopWatch(); stopWatch.start();
	 * 
	 * try { log.info("record message: {}", record); String message =
	 * record.value(); log.info("Consumed message: {}", message);
	 * acknowledgment.acknowledge();
	 * 
	 * stopWatch.stop(); log.info("Time taken to process message: {} ms",
	 * stopWatch.getTotalTimeMillis()); } catch (Exception ex) { stopWatch.stop();
	 * log.error("Error processing message from topic {}: {} in {} ms", topic,
	 * record.value(), stopWatch.getTotalTimeMillis(), ex);
	 * 
	 * } }
	 */
    @KafkaListener(topics = "${kafka.topic.user-image}", groupId = "${kafka.consumer.group-id}")
    public void consumeMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            String message = record.value();
            log.info("Raw Kafka message: {}", message);
           // Deserialize the JSON message
            ObjectMapper mapper = new ObjectMapper();
            ProducerMessage producerMessage = mapper.readValue(message, ProducerMessage.class);

            // Decode the Base64-encoded image bytes
           // byte[] imageBytes = Base64.getDecoder().decode(producerMessage.getImageBytes());
            
         // Decode Base64-encoded image data
            byte[] imageBytes = Base64.getDecoder().decode(producerMessage.getImageBytesString());
            
            // Save or process the image bytes
            String filePath = "C:/temp/uploads/" + producerMessage.getImageName();
            Files.write(Paths.get(filePath), imageBytes);

            log.info("Image saved successfully to {}", filePath);
            acknowledgment.acknowledge();
        } catch (JsonMappingException ex) {
        	log.info("Consumed message: {}");
       	    acknowledgment.acknowledge();
        }catch(JsonParseException ex) {
        	log.info("Consumed message: {}");
       	    acknowledgment.acknowledge();
        }
        catch (Exception ex) {
            log.error("Error processing Kafka message", ex);
        }
    }

}
