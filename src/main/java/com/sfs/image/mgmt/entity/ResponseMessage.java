package com.sfs.image.mgmt.entity;

import org.springframework.boot.jackson.JsonComponent;

import lombok.Data;

@Data
public class ResponseMessage {

	  private Long id;
	  private String user;
	  private String imgurId;
	  private String imageName;
	  private String imageLink;
	  private String message;
}
