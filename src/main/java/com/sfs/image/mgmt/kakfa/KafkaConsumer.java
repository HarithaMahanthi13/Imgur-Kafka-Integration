package com.sfs.image.mgmt.kakfa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfs.image.mgmt.entity.ProducerMessage;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

    @Value("${image.save.path:C:/temp/uploads}")
    private String savePath;

    public KafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Listens to the Kafka topic for incoming messages and processes them.
     *
     * @param record the Kafka record containing the image data as JSON
     */
    @KafkaListener(topics = "${kafka.topic.user-image}", groupId = "${kafka.consumer.group-id}")
    public void consumeImage(ConsumerRecord<String, String> record) {
        log.info("Received message from Kafka: {}", record.value());

        try {
        	
        	
            // Deserialize the message to a ProducerMessage object
            ProducerMessage message = objectMapper.readValue(record.value(), ProducerMessage.class);

            // Save the image locally
            saveImageLocally(message);

        } 
        catch(JsonProcessingException e) {
        	log.info("received text");
        	
        }catch (IOException e) {
            log.error("Error processing message: {}", record.value(), e);
        }
    }

    /**
     * Saves the image locally on the filesystem.
     *
     * @param message the ProducerMessage containing the image data
     */
    private void saveImageLocally(ProducerMessage message) {
        try {
            // Ensure the directory exists
            File directory = new File(savePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Decode Base64 image bytes
            byte[] imageBytes = Base64.getDecoder().decode(message.getImageBytesString());

            // Create the file and write the image bytes
            File file = new File(directory, message.getImageName());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(imageBytes);
            }

            log.info("Image saved successfully: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error saving image locally", e);
        }
    }
}
