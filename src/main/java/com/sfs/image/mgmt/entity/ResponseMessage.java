package com.sfs.image.mgmt.entity;

import lombok.Data;

/**
 * Entity class representing the response message to be sent back to the client.
 */
@Data
public class ResponseMessage {

    /**
     * The unique identifier for the image.
     */
    private Long id;

    /**
     * The username of the user who uploaded the image.
     */
    private String user;

    /**
     * The unique identifier for the image on Imgur.
     */
    private String imgurId;

    /**
     * The name of the image.
     */
    private String imageName;

    /**
     * The URL link to access the image on Imgur.
     */
    private String imageLink;

    /**
     * The message to be sent back to the client.
     */
    private String message;
}
