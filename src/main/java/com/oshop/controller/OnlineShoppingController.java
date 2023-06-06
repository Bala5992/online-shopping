package com.oshop.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oshop.enumeration.AuthHeaderEnum;
import com.oshop.model.CategoryVO;
import com.oshop.model.NewProductVO;
import com.oshop.model.request.Searchable;
import com.oshop.model.response.GenericResponse;
import com.oshop.service.impl.CategoryServiceImpl;
import com.oshop.service.impl.ProductServiceImpl;
import com.oshop.service.impl.ShoppingServiceImpl;
import com.oshop.utils.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/")
@Validated
@Slf4j
public class OnlineShoppingController {
	
	private final ShoppingServiceImpl adminService;
	
	private final CategoryServiceImpl categoryService;
	
	private final ProductServiceImpl productService;
	
	public OnlineShoppingController(ShoppingServiceImpl adminService, CategoryServiceImpl categoryService,
			ProductServiceImpl productService) {
		this.adminService = adminService;
		this.categoryService = categoryService;
		this.productService = productService;
	}

	@PostMapping("products")
	@Operation(summary = "Create a new product")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Product created successfully", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = NewProductVO.class)) }),
	  @ApiResponse(responseCode = "400", description = "Invalid product details", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }), 
	  @ApiResponse(responseCode = "404", description = "Category not found", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }), 
	  @ApiResponse(responseCode = "503", description = "Insufficient privilege", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }) })
	public ResponseEntity<GenericResponse<List<NewProductVO>>> createProduct(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum, 
			@Parameter(description = "list of products to be created") @RequestBody List<@Valid NewProductVO> productVOs) {
		log.info("Controller - Request started for create product");
		ResponseEntity<GenericResponse<List<NewProductVO>>> responseEntity = ResponseEntity.ok(adminService.createAllProduct(productVOs));
		log.info("Controller - Request completed for create product");
		return responseEntity;
	}
	
	@PutMapping("products")
	@Operation(summary = "Update an existing product")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Product updated successfully", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = NewProductVO.class)) }),
	  @ApiResponse(responseCode = "400", description = "Invalid product details", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }), 
	  @ApiResponse(responseCode = "404", description = "Product not found", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }),
	  @ApiResponse(responseCode = "503", description = "Insufficient privilege", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }) })
	public ResponseEntity<GenericResponse<List<NewProductVO>>> updateProduct(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum,
			@Parameter(description = "list of products to be updated") @RequestBody List<@Valid NewProductVO> productVOs) {
		log.info("Controller - Request started for update product");
		ResponseEntity<GenericResponse<List<NewProductVO>>> responseEntity = ResponseEntity.ok(adminService.updateAllProduct(productVOs));
		log.info("Controller - Request completed for update product");
		return responseEntity;
	}
	
	@PostMapping("categories")
	@Operation(summary = "Create a new category")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Category created successfully", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = NewProductVO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid category details", 
			content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = GenericResponse.class)) }), 
			@ApiResponse(responseCode = "503", description = "Insufficient privilege", 
			content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = GenericResponse.class))  } )})
	public ResponseEntity<GenericResponse<List<CategoryVO>>> createCategory(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum, 
			@Parameter(description = "list of categories to be created") @RequestBody List<@Valid CategoryVO> categoryVOs) {
		log.info("Controller - Request started for create category");
		ResponseEntity<GenericResponse<List<CategoryVO>>> responseEntity = ResponseEntity.ok(categoryService.add(categoryVOs));
		log.info("Controller - Request completed for create category");
		return responseEntity;
	}
	
	@GetMapping("products")
	@Operation(summary = "List all products")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Products found", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = NewProductVO.class)) })})
	public ResponseEntity<GenericResponse<List<NewProductVO>>> listProducts(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum,
			@ParameterObject Pageable pageable, @ParameterObject Searchable searchable) {
		log.info("Controller - Request started for list all product");
		ResponseEntity<GenericResponse<List<NewProductVO>>> responseEntity = ResponseEntity.ok(adminService.listProducts(pageable, searchable));
		log.info("Controller - Request completed for list all product");
		return responseEntity;
	}
	
	@GetMapping("products/{id}")
	@Operation(summary = "Get a product by id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Found product for the given id", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = NewProductVO.class)) }),
			@ApiResponse(responseCode = "404", description = "Product not found", 
		    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  })})
	public ResponseEntity<GenericResponse<NewProductVO>> getProduct(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum, 
			@Parameter(description = "id of product to be searched") @PathVariable("id") int id) {
		log.info("Controller - Request started for retrieve a product by id");
		ResponseEntity<GenericResponse<NewProductVO>> responseEntity = ResponseEntity.ok(productService.get(id));
		log.info("Controller - Request completed for retrieve a product by id");
		return responseEntity;
	}
	
	@GetMapping("products/categories")
	@Operation(summary = "List all catagories")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Category(s) found", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = NewProductVO.class)) })})
	public ResponseEntity<GenericResponse<List<CategoryVO>>> listCategories(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum) {
		log.info("Controller - Request started for list all categories");
		ResponseEntity<GenericResponse<List<CategoryVO>>> responseEntity = ResponseEntity.ok(adminService.listCategories());
		log.info("Controller - Request completed for list all categories");
		return responseEntity;
	}
	
	@GetMapping("products/category")
	@Operation(summary = "List all products under the given category")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Found products for the given category", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = NewProductVO.class)) })})
	public ResponseEntity<GenericResponse<List<NewProductVO>>> listCategories(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum,
			@Parameter(description = "name of category to be searched") @RequestParam("name") String name, @ParameterObject Searchable searchable) {
		log.info("Controller - Request started for list all products by category");
		ResponseEntity<GenericResponse<List<NewProductVO>>> responseEntity = ResponseEntity.ok(adminService.listProductsForCategory(name, searchable));
		log.info("Controller - Request completed for list all products by category");
		return responseEntity;
	}
	
	@DeleteMapping("categories/{id}")
	@Operation(summary = "Delete a category by id")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Category deleted successfully", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }),
	  @ApiResponse(responseCode = "404", description = "Category not found", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }), 
	  @ApiResponse(responseCode = "503", description = "Insufficient privilege", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }) })
	public ResponseEntity<GenericResponse<String>> deleteCategory(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum,
			@Parameter(description = "id of category to be deleted") @PathVariable int id) {
		log.info("Controller - Request started for delete a product");
		ResponseEntity<GenericResponse<String>> responseEntity = ResponseEntity.ok(categoryService.delete(id));
		log.info("Controller - Request completed for delete a product");
		return responseEntity;
	}
	
	@DeleteMapping("products/{id}")
	@Operation(summary = "Delete a product by id")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Product deleted successfully", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }),
	  @ApiResponse(responseCode = "404", description = "Product not found", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }), 
	  @ApiResponse(responseCode = "503", description = "Insufficient privilege", 
	    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))  }) })
	public ResponseEntity<GenericResponse<String>> deleteProduct(@RequestHeader(value = AppConstants.AUTH_PARAM_NAME, required = true) AuthHeaderEnum headerEnum, 
			@Parameter(description = "id of product to be deleted") @PathVariable int id) {
		log.info("Controller - Request started for delete a category");
		ResponseEntity<GenericResponse<String>> responseEntity = ResponseEntity.ok(productService.delete(id));
		log.info("Controller - Request completed for delete a category");
		return responseEntity;
	}
}
