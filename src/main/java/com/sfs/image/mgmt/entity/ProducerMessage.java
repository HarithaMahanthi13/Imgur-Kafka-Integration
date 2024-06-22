package com.sfs.image.mgmt.entity;

import lombok.Data;

@Data
public class ProducerMessage {

    private String username;
    private String imageName;
    private byte[] imageBytes;


}

