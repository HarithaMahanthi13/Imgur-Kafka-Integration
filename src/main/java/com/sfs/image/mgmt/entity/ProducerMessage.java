package com.sfs.image.mgmt.entity;

import lombok.Data;

/**
 * Entity class representing a message to be sent to the Kafka producer.
 */
@Data
public class ProducerMessage {

    /**
     * The username of the user who uploaded the image.
     */
    private String username;

    /**
     * The name of the image.
     */
    private String imageName;

    /**
     * The byte array representing the image data.
     */
    private byte[] imageBytes;
}
