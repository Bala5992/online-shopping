package com.oshop.model;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter@Getter
public class NewProductVO extends ProductVO {
	
	public NewProductVO(int id, @NotBlank(message = "Product Name should not be blank") String name,
			@NotBlank(message = "Product Description should not be blank") String description,
			@NotBlank(message = "Product Brand should not be blank") String brand, int categoryId, @Valid ProductPriceVO price, @Valid InventoryVO inventory,
			@Size(min = 1, message = "Atleast one attribute is required") @NotNull(message = "Product Attribute is required") Set<@Valid ProductAttributeVO> attributes) {
		super(id, name, description, brand, categoryId);
		this.price = price;
		this.inventory = inventory;
		this.attributes = attributes;
	}

	@Valid
	private ProductPriceVO price;
	
	@Valid
	private InventoryVO inventory;
	
	@Size(min = 1, message = "Atleast one attribute is required")
	@NotNull(message = "Product Attribute is required")
	private Set<@Valid ProductAttributeVO> attributes;
}
