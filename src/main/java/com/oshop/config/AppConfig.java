package com.oshop.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper mapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);
		return mapper;
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
}
