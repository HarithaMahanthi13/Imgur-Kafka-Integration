package com.sfs.image.mgmt.security;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
public class JWTPropertyHolder implements Serializable {
	
	private static final long serialVersionUID=66;
	private String authority;
	private String tokenPath;
	private String clientId;
	private String resource;
	private String secretkey;
	
	

}
