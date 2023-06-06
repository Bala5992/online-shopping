package com.oshop.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
public class CategoryVO {

	private int id;
	
	@NotBlank(message = "Category Name should not be blank")
	private String name;
	
	@NotBlank(message = "Category Description should not be blank")
	private String description;
}
