package com.oshop.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
public class ProductVO {

	private int id;
	
	@NotBlank(message = "Product Name should not be blank")
	private String name;
	
	@NotBlank(message = "Product Description should not be blank")
	private String description;
	
	@NotBlank(message = "Product Brand should not be blank")
	private String brand;
	
	private int categoryId;
	
}
