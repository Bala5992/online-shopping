package com.oshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
public class InventoryVO {

	private int id;
	
	private int productId;
	
	private int total;
	
	private int available;
	
	private int reserved;
	
	@JsonIgnore
	private ProductVO product; 
}
