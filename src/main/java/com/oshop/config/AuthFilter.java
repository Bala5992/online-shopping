package com.oshop.config;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oshop.enumeration.AuthHeaderEnum;
import com.oshop.enumeration.Status;
import com.oshop.model.response.GenericResponse;
import com.oshop.utils.AppConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("Request entered auth filter");
		if (Stream.of(AppConstants.SECURED_PATHS).anyMatch(path -> request.getRequestURI().startsWith(path))) {
			log.info("Validating secured paths");
			String authHeaderValue = request.getHeader(AppConstants.AUTH_PARAM_NAME);
			log.debug("Auth header value : {}",  authHeaderValue);
			if (StringUtils.isEmpty(authHeaderValue)) {
				log.error(AppConstants.AUTH_HEADER_NOT_AVAILABLE);
				buildError(response, AppConstants.AUTH_HEADER_NOT_AVAILABLE);
			} else if (AuthHeaderEnum.USER.name().equals(authHeaderValue) && !HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
				log.error(AppConstants.USER_NOT_AUTHORIZED);
				buildError(response, AppConstants.USER_NOT_AUTHORIZED);
			} else {
				log.info("User authenticated successfully");
				log.debug("Successfully authenticated with role : {}", authHeaderValue);
				filterChain.doFilter(request, response);
				log.info("Request exiting auth filter");
			}
		} else {
			log.info("By-passing path : [{}]", request.getRequestURI());
			filterChain.doFilter(request, response);
			log.info("Request exiting auth filter");
		}
	}

	private void buildError(HttpServletResponse response, String message) throws IOException, JsonProcessingException {
		GenericResponse<String> genericResponse = new GenericResponse<>();
		genericResponse.header(Status.FAILED, HttpStatus.FORBIDDEN, message);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(AppConstants.HTTP_CONTENT_TYPE_JSON);
		response.getWriter().write(mapper.writeValueAsString(genericResponse));
	}

}
