package com.oshop.model.response;

import com.oshop.enumeration.Status;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Generated
public class ResponseHeader {
	
	private Status status;
	
	private String code;
	
	private String message;
	
	private String traceId;
	
	private String timestamp;

}
