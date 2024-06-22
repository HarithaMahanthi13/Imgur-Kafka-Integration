package com.sfs.image.mgmt.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfs.image.mgmt.entity.ProducerMessage;
import com.sfs.image.mgmt.kakfaProducer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class KafkaImageUploadService {

    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaImageUploadService(KafkaProducer kafkaProducer,ObjectMapper objectMapper) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public void uploadImage(String username, String imageName,MultipartFile file) throws IOException {
    	ProducerMessage producerMessage=new ProducerMessage();
    	producerMessage.setUsername(username);
    	producerMessage.setImageName(imageName);
    	producerMessage.setImageBytes(file.getBytes());
    	
      
    	 try {
             String jsonMessage = objectMapper.writeValueAsString(producerMessage);
             kafkaProducer.sendMessage(jsonMessage);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
           
         }
    }
}

