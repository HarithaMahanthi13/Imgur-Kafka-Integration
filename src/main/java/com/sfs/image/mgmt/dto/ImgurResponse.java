package com.sfs.image.mgmt.dto;

import lombok.Data;

/**
 * DTO class representing the response from the Imgur API after an image upload.
 */
@Data
public class ImgurResponse {
    /**
     * The data returned by the Imgur API, including the ID and link of the uploaded image.
     */
    private ImgurData data;
}
