package com.oshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
public class ProductPriceVO {

	private int id;
	
	private int productId;
	
	private String currency;
	
	@Min(message = "Amount should be greater than 0", value = 1)
	private float amount;
	
	@JsonIgnore
	private ProductVO product; 
}
