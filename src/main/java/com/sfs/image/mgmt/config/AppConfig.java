package com.sfs.image.mgmt.config;

import java.net.URISyntaxException;

import javax.cache.Caching;

import  javax.cache.CacheManager;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {
	
	
	
	
	  @Bean
	  public RestTemplate restTemplate() {
	        return new RestTemplate();
	  }
	  
	  
	 
}
