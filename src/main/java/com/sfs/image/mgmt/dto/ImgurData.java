package com.sfs.image.mgmt.dto;

import lombok.Data;

/**
 * DTO class representing the data returned by the Imgur API.
 */
@Data
public class ImgurData {
    /**
     * The unique identifier for the uploaded image on Imgur.
     */
    private String id;

    /**
     * The URL link to access the uploaded image on Imgur.
     */
    private String link;
}
