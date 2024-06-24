package com.sfs.image.mgmt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfs.image.mgmt.entity.ProducerMessage;
import com.sfs.image.mgmt.kakfa.KafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaImageUploadService {

    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaImageUploadService(KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    /**
     * Uploads an image and sends a message to Kafka.
     *
     * This method creates a ProducerMessage object with the username, image name, and image bytes from the provided MultipartFile.
     * It then serializes the ProducerMessage object to a JSON string and sends it to Kafka using the KafkaProducer.
     * If there is an error in processing the JSON message, it returns an error message.
     *
     * @param username the username of the user uploading the image
     * @param imageName the name of the image
     * @param file the MultipartFile containing the image
     * @return a status message indicating the result of the Kafka message send operation
     * @throws IOException if an I/O error occurs while reading the image bytes
     */
    @Async("taskExecutor")
    public String uploadImage(String username, String imageName, MultipartFile file) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ProducerMessage producerMessage = new ProducerMessage();
        producerMessage.setUsername(username);
        producerMessage.setImageName(imageName);
        producerMessage.setImageBytes(file.getBytes());

        try {
            String jsonMessage = objectMapper.writeValueAsString(producerMessage);
            String kafkaStatus = kafkaProducer.sendMessage(jsonMessage);
            return kafkaStatus;
        } catch (JsonProcessingException e) {
            log.error("Error in Kafka message processing: " + e.getMessage(), e);
            return "Error in Kafka message processing";
        } finally {
            stopWatch.stop();
            log.info("Time taken to upload image and send message to Kafka: " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }
}
