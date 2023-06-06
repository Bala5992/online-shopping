package com.oshop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oshop.entity.Category;
import com.oshop.entity.Product;
import com.oshop.enumeration.InventoryStockEnum;
import com.oshop.enumeration.PriceEnum;
import com.oshop.enumeration.Status;
import com.oshop.exception.NotFoundException;
import com.oshop.model.CategoryVO;
import com.oshop.model.NewProductVO;
import com.oshop.model.ProductVO;
import com.oshop.model.request.Searchable;
import com.oshop.model.response.GenericResponse;
import com.oshop.model.response.ResponseError;
import com.oshop.repository.CategoryRepository;
import com.oshop.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingServiceImpl {

	private final ModelMapper mapper; 
	
	private final ProductServiceImpl productServiceImpl; 
	
	private final CategoryServiceImpl categoryServiceImpl;
	
	private final ProductRepository productRepository;
	
	private final CategoryRepository categoryRepository;
	
	public ShoppingServiceImpl(ModelMapper mapper, ProductServiceImpl productService, CategoryServiceImpl categoryService, 
			ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.mapper = mapper;
		this.productServiceImpl = productService;
		this.categoryServiceImpl = categoryService;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}
	
	public GenericResponse<List<NewProductVO>> createAllProduct(List<NewProductVO> newProductVOs) throws NotFoundException {
		log.info("[Service] Request entered create all product service");
		if (Objects.nonNull(newProductVOs) && newProductVOs.size() > 0) {
			List<ResponseError> errors = new ArrayList<>();
			List<NewProductVO> successList = populateAndCreateProduct(newProductVOs, errors);
			GenericResponse<List<NewProductVO>> genericResponse = new GenericResponse<List<NewProductVO>>();
			genericResponse.header(Status.SUCCESS, HttpStatus.OK, String.format("%d Product(s) created", successList.size()));
			genericResponse.setErrors(errors);
			genericResponse.setPayload(successList);
			log.info("[Service] [CREATE] Process completed successfully for {} product(s) with {} error(s)", successList.size(), errors.size());
			return genericResponse;
		} else {
			GenericResponse<List<NewProductVO>> genericResponse = new GenericResponse<List<NewProductVO>>();
			genericResponse.header(Status.SUCCESS, HttpStatus.OK, "No Product(s) to add");
			log.info("[Service] [CREATE] No product(s) to process");
			return genericResponse;
		}
	}
	
	public GenericResponse<List<NewProductVO>> updateAllProduct(List<NewProductVO> newProductVOs) throws NotFoundException {
		log.info("[Service] Request entered update product service");
		if (Objects.nonNull(newProductVOs) && newProductVOs.size() > 0) {
			List<ResponseError> errors = new ArrayList<>();
			List<NewProductVO> successList = populateAndCreateProduct(newProductVOs, errors);
			GenericResponse<List<NewProductVO>> genericResponse = new GenericResponse<List<NewProductVO>>();
			genericResponse.header(Status.SUCCESS, HttpStatus.OK, String.format("%d Product(s) updated", successList.size()));
			genericResponse.setErrors(errors);
			genericResponse.setPayload(successList);
			log.info("[Service] [UPDATE] Process completed successfully for {} product(s) with {} error(s)", successList.size(), errors.size());
			return genericResponse;
		} else {
			GenericResponse<List<NewProductVO>> genericResponse = new GenericResponse<List<NewProductVO>>();
			genericResponse.header(Status.SUCCESS, HttpStatus.OK, "No Product(s) to updated");
			log.info("[Service] [UPDATE] No product(s) to process");
			return genericResponse;
		}
	}
	
	public List<NewProductVO> populateAndCreateProduct(List<NewProductVO> newProductVOs, List<ResponseError> errors) {
		log.info("[Service] Request entered create product service");
		List<NewProductVO> successProductList = new LinkedList<>();
		newProductVOs.stream().forEach(newProductVO -> {
			try {
				categoryServiceImpl.get(newProductVO.getCategoryId());
			} catch (NotFoundException exception) {
				ResponseError error = new ResponseError(newProductVO.getName(), String.format("Category does not exist with id : %d", newProductVO.getCategoryId()));
				errors.add(error);
				log.debug("Category does not exist with id : %d", newProductVO.getCategoryId());
				return;
			}
			Optional<Product> existingProduct = productRepository.findByName(newProductVO.getName());
			if (existingProduct.isPresent() && newProductVO.getId() == 0) {
				ResponseError error = new ResponseError(newProductVO.getName(), "Product already exists");
				errors.add(error);
				log.debug("Product already exists [%s]", newProductVO.getName());
				return;
			}
			ProductVO product = mapper.map(newProductVO, ProductVO.class);
			log.debug("Populating product line details for [{}]", newProductVO.getName());
			newProductVO.getAttributes().forEach(attr -> attr.setProduct(product));
			if (Objects.nonNull(newProductVO.getPrice())) {
				newProductVO.getPrice().setProduct(product);
			}
			if (Objects.nonNull(newProductVO.getInventory())) {
				newProductVO.getInventory().setProduct(product);
			}
			GenericResponse<NewProductVO> genericProductResponse = productServiceImpl.add(newProductVO);
			if (Objects.nonNull(genericProductResponse.getPayload())) {
				successProductList.add(genericProductResponse.getPayload());
			}
		});
		return successProductList;
	}

	public GenericResponse<List<NewProductVO>> listProducts(Pageable pageable, Searchable searchable) {
		log.info("[Service] Request entered list all prodcuts service");
		GenericResponse<List<NewProductVO>> genericResponse = new GenericResponse<List<NewProductVO>>();
		List<NewProductVO> list = productRepository.findAll(pageable).stream().map(product -> mapper.map(product, NewProductVO.class)).toList();		
		List<NewProductVO> processed = filterForLimitedProducts(list, genericResponse, searchable); 
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, processed.isEmpty() ? "No product found" : String.format("%d Product(s) listed", processed.size()));
		genericResponse.setPayload(processed);
		log.info("[Service] Successfully retrieved {} product(s) with criteria {}", processed.size(), searchable);
		return genericResponse;
	}

	public GenericResponse<List<CategoryVO>> listCategories() {
		return categoryServiceImpl.get();
	}

	public GenericResponse<List<NewProductVO>> listProductsForCategory(String name, Searchable searchable) {
		log.info("[Service] Request entered list all products for category service");
		log.debug("Search started with category [{}]", name);
		Optional<Category> categoryOpt = categoryRepository.findByName(name);
		if (categoryOpt.isPresent()) {
			log.debug("Category [{}] found", name);
			GenericResponse<List<NewProductVO>> genericResponse = new GenericResponse<List<NewProductVO>>();
			
			List<NewProductVO> list = productRepository.findAllByCategoryId(categoryOpt.get().getId()).stream().map(product -> mapper.map(product, NewProductVO.class)).toList();
			List<NewProductVO> processed = filterForLimitedProducts(list, genericResponse, searchable); 
			genericResponse.header(Status.SUCCESS, HttpStatus.OK, processed.isEmpty() ? String.format("No product found for category : %s", name) : String.format("%d Product(s) listed for category : %s", processed.size(), name));
			genericResponse.setPayload(processed);
			log.info("[Service] Successfully retrieved {} product(s) for {} with criteria {}", processed.size(), name, searchable);
			return genericResponse;
		} else {
			log.debug("Category [{}] not found", name);
			throw new NotFoundException(String.format("Category not found : %s", name));
		}
	}
	
	private List<NewProductVO> filterForLimitedProducts(List<NewProductVO> list, GenericResponse<List<NewProductVO>> genericResponse, Searchable searchable) {
		log.info("[Service] Request entered internal filter");
		log.debug("Search started for {}", searchable);
		List<ResponseError> errors = new ArrayList<>();
		if (!Optional.ofNullable(list).isEmpty() && Objects.nonNull(searchable)) {
			List<NewProductVO> processed = list.stream().filter(product -> {
				if (Objects.nonNull(product.getInventory())) {
					if (InventoryStockEnum.AVAILABLE.equals(searchable.getInventory())) {
//						if (product.getInventory().getAvailable() > product.getInventory().getReserved()) {
							return true;
//						} else if (product.getInventory().getAvailable() <= product.getInventory().getReserved()) {
//							errors.add(new ResponseError(product.getName(), "Limited stock"));
//							return false;
//						}
					} else if (InventoryStockEnum.OUT_OF_STOCK.equals(searchable.getInventory())) {
						return product.getInventory().getAvailable() == 0;
					} else if (InventoryStockEnum.LIMITED.equals(searchable.getInventory())) {
						return product.getInventory().getAvailable() <= product.getInventory().getReserved();
					}
				}
				log.debug("Inventory data is empty for [{}]", product.getName());
				errors.add(new ResponseError(product.getName(), "Inventory data not available"));
				return false;
			}).filter(product -> {
				if (Objects.nonNull(product.getPrice())) {
					if (Objects.nonNull(searchable.getPriceRange()) && searchable.getPriceRange().indexOf(",") != -1) {
						String[] priceRange = searchable.getPriceRange().split(",");
						
						if (product.getPrice().getAmount() >= Float.parseFloat(priceRange[0]) && product.getPrice().getAmount() <= Float.parseFloat(priceRange[1])) {
							return true;
						}
						return false;
					} else {
						return true;
					}
				}
				log.debug("Price data is empty for [{}]", product.getName());
				errors.add(new ResponseError(product.getName(), "Price data not available"));
				return false;
			}).sorted((obj1, obj2) -> {
				return searchable.getPrice().equals(PriceEnum.LOW) ? Float.valueOf(obj1.getPrice().getAmount()).compareTo(obj2.getPrice().getAmount()) : 
					Float.valueOf(obj2.getPrice().getAmount()).compareTo(obj1.getPrice().getAmount()); 
			}) .collect(Collectors.toList());
			genericResponse.setErrors(errors);
			log.debug("Search completed for criteria {} with {} product(s)", searchable, processed.size());
			return processed;
		}	
		return Collections.emptyList();
	}

}
