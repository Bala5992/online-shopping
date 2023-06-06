package com.oshop.model.response;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oshop.enumeration.Status;
import com.oshop.utils.AppConstants;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Generated
public class GenericResponse<T> {

	private ResponseHeader header;
	
	@JsonInclude(value = Include.NON_EMPTY)
	private T payload;
	
	@JsonInclude(value = Include.NON_EMPTY)
	private List<ResponseError> errors;

	public void header(Status status, HttpStatus httpStatus, String message) {
		header = new ResponseHeader(status, httpStatus.toString(), message, MDC.get("traceId"), LocalDateTime.now().format(AppConstants.APP_DATE_TIME_FORMATTER));
	}

}
