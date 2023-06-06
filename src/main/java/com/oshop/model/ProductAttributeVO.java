package com.oshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
public class ProductAttributeVO {

	private int id;
	
	private int productId;
	
	@NotBlank(message = "Attribute Name should not be blank")
	private String name;
	
	@NotBlank(message = "Attribute Value should not be blank")
	private String value;
	
	@JsonIgnore
	private ProductVO product;
}
