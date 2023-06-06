package com.oshop.model.request;

import com.oshop.enumeration.InventoryStockEnum;
import com.oshop.enumeration.PriceEnum;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

@Data
@AllArgsConstructor
@Generated
public class Searchable {

	@Parameter(required = true, example = "AVAILABLE")
	private InventoryStockEnum inventory;
	
	@Parameter(required = true, example = "LOW")
	private PriceEnum price;
	
	@Parameter(description = "Price range in the format: min,max")
	private String priceRange;
}
