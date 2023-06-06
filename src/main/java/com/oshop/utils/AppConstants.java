package com.oshop.utils;

import java.time.format.DateTimeFormatter;

import lombok.Generated;

@Generated
public class AppConstants {

	public static final String AUTH_PARAM_NAME = "X-AUTH";
	public static final String HTTP_CONTENT_TYPE_JSON = "application/json";
	public static final String AUTH_HEADER_NOT_AVAILABLE = "Auth header is not available";
	public static final String USER_NOT_AUTHORIZED = "USER not authorized to do this operation";	
	public static final String FIELD_VALIDATIONS_MESSAGE = "Fields validation failed";
	
	public static final String APP_DATE_TIME_FORMAT = "dd-MMM-yyyy hh:mm:ss a";
	public static final DateTimeFormatter APP_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(APP_DATE_TIME_FORMAT);
	
	public static final String[] SECURED_PATHS = new String[] {"/api"};
}
