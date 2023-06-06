package com.oshop.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import com.oshop.entity.Category;
import com.oshop.entity.Inventory;
import com.oshop.entity.Product;
import com.oshop.entity.ProductAttribute;
import com.oshop.entity.ProductPrice;
import com.oshop.enumeration.InventoryStockEnum;
import com.oshop.enumeration.PriceEnum;
import com.oshop.enumeration.Status;
import com.oshop.model.CategoryVO;
import com.oshop.model.InventoryVO;
import com.oshop.model.NewProductVO;
import com.oshop.model.ProductAttributeVO;
import com.oshop.model.ProductPriceVO;
import com.oshop.model.ProductVO;
import com.oshop.model.request.Searchable;
import com.oshop.model.response.GenericResponse;

public class AppFixtures {

	public static final String SUCCESS_MSG = "Success";
	public static final int PRODUCT_ID = 1;
	public static final int CATEGORY_ID = 1;
	public static final String CATEGORY_NAME = "Electronic Watch";
	public static final String PRODUCT_NAME = "Apple Watch";
	
	public static final String PAYLOAD_INVALID_JSON = "[{\"id\": 0,\"name\": \"Smartphones\",\"description\": \"smartphones\"},]";
	
	public static final String AUTH_HEADER_NOT_AVAILABLE = "Auth header is not available";
	public static final String USER_NOT_AUTHORIZED = "USER not authorized to do this operation";
	public static final String FIELD_VALIDATIONS_MESSAGE = "Fields validation failed";
	
	public static final ProductPriceVO PRICE_VO = new ProductPriceVO(200, 1,  "USD", 2000, null);
	public static final InventoryVO INVENTORY_VO = new InventoryVO(100, 1, 10, 0, 5, null);
	
	public static final ProductPriceVO PRICE_VO_1 = new ProductPriceVO(201, 1,  "USD", 1000, null);
	public static final InventoryVO INVENTORY_VO_2 = new InventoryVO(101, 1, 10, 0, 5, null);
	
	public static final ProductAttributeVO ATTRIBUTE_VO1 = new ProductAttributeVO(300, 1, "Size", "Medium", null);
	public static final ProductAttributeVO ATTRIBUTE_VO2 = new ProductAttributeVO(301, 1, "Color", "Red", null);
	public static final ProductAttributeVO ATTRIBUTE_VO3 = new ProductAttributeVO(302, 1, "Strap Color", "Red with white", null);

	public static final ProductAttributeVO ATTRIBUTE_VO4 = new ProductAttributeVO(303, 1, "Processor", "Intel core i7", null);
	public static final ProductAttributeVO ATTRIBUTE_VO5 = new ProductAttributeVO(304, 1, "Ram", "8GB", null);
	
	public static final CategoryVO CATEGORY_VO = new CategoryVO(1, CATEGORY_NAME, "All brand electronic watches will be listed here");
	public static final NewProductVO NEW_PRODUCT_VO = new NewProductVO(1, PRODUCT_NAME, "Smart watch for all ages peoples", "Apple", 1, PRICE_VO, INVENTORY_VO, 
			new HashSet<>(Arrays.asList(ATTRIBUTE_VO2, ATTRIBUTE_VO4, ATTRIBUTE_VO5)));
	public static final ProductVO PRODUCT_VO = new ProductVO(0, PRODUCT_NAME, "Apple smart watch", "Apple", 1);
	
	public static final NewProductVO NEW_PRODUCT_VO1 = new NewProductVO(2, "Wireless headphones", "Hear smartly", "Bose", 1, PRICE_VO_1, INVENTORY_VO_2, 
			new HashSet<>(Arrays.asList(ATTRIBUTE_VO1, ATTRIBUTE_VO2, ATTRIBUTE_VO3)));
	
	public static final NewProductVO NEW_PRODUCT_VO2 = new NewProductVO(3, "Laptop", "Work from home easily", "Dell", 1, null, INVENTORY_VO_2, 
			new HashSet<>(Arrays.asList(ATTRIBUTE_VO4, ATTRIBUTE_VO5)));
	public static final NewProductVO NEW_PRODUCT_VO3 = new NewProductVO(4, "TV", "Smart TV", "Sony", 1, PRICE_VO_1, null, 
			new HashSet<>(Arrays.asList(ATTRIBUTE_VO5)));
	
	public static final NewProductVO NEW_PRODUCT_VO4 = new NewProductVO(5, "Home Theatre", "Dolby audio in homes", "Onkyo", 1, PRICE_VO_1, null, null);
	
	public static final ProductAttribute ATTRIBUTE_1 = new ProductAttribute(300, "Size", "Medium", null);
	public static final ProductAttribute ATTRIBUTE_2 = new ProductAttribute(301, "Color", "Red", null);
	public static final ProductAttribute ATTRIBUTE_3 = new ProductAttribute(302, "Strap Color", "Red with white", null);
	public static final ProductAttribute ATTRIBUTE_4 = new ProductAttribute(303, "Processor", "Intel core i7", null);
	public static final ProductAttribute ATTRIBUTE_5 = new ProductAttribute(304, "Ram", "8GB", null);
	
	public static final ProductPrice PRICE_1 = new ProductPrice(200, "USD", 2000, null);
	public static final Inventory INVENTORY_1 = new Inventory(100, 10, 0, 5, null);
	
	public static final ProductPrice PRICE_2 = new ProductPrice(201, "USD", 1000, null);
	public static final Inventory INVENTORY_2 = new Inventory(101, 10, 0, 5, null);
	
	public static final Category CATEGORY_1 = new Category(1, CATEGORY_NAME, "All brand electronic watches will be listed here", null);
	public static final Category CATEGORY_2 = new Category(2, "Mens Fashion", "All branded and non brand apperals will be listed here", null);
	public static final Product PRODUCT = new Product(1, PRODUCT_NAME, "Apple smart watch", "Apple", 1, PRICE_1, INVENTORY_1, Arrays.asList(ATTRIBUTE_1, ATTRIBUTE_2, ATTRIBUTE_3));
	public static final Product PRODUCT_1 = new Product(2, "Wireless headphones", "Hear smartly", "Bose", 1, PRICE_2, INVENTORY_2, Arrays.asList(ATTRIBUTE_1, ATTRIBUTE_2, ATTRIBUTE_3));
	public static final Product PRODUCT_2 = new Product(3, "Laptop", "Work from home easily", "Dell", 1, null, INVENTORY_2, Arrays.asList(ATTRIBUTE_4, ATTRIBUTE_5));
	public static final Product PRODUCT_3 = new Product(4, "TV", "Smart TV", "Sony", 1, PRICE_2, null, Arrays.asList(ATTRIBUTE_5));
	
	public static final GenericResponse<NewProductVO> GENERIC_PRODUCT_RESPONSE;
	public static final GenericResponse<List<NewProductVO>> GENERIC_PRODUCT_RESPONSE_LIST;
	public static final GenericResponse<CategoryVO> GENERIC_CATEGORY_RESPONSE;
	public static final GenericResponse<List<CategoryVO>> GENERIC_CATEGORY_RESPONSE_LIST;
	
	public static final GenericResponse<String> GENERIC_CATEGORY_DELETE_RESPONSE;
	public static final GenericResponse<String> GENERIC_PRODUCT_DELETE_RESPONSE;
	
	public static final List<NewProductVO> NEW_PRODUCT_VO_LIST = Arrays.asList(NEW_PRODUCT_VO);
	public static final List<CategoryVO> CATEGORY_VO_LIST = Arrays.asList(CATEGORY_VO);
	public static final List<Product> PRODUCT_LIST = Arrays.asList(PRODUCT);
	public static final List<Product> PRODUCT_LIST_1 = Arrays.asList(PRODUCT_1, PRODUCT);
	public static final List<Product> PRODUCT_LIST_2 = Arrays.asList(PRODUCT_2);
	public static final List<Product> PRODUCT_LIST_3 = Arrays.asList(PRODUCT_3);
	public static final List<Category> CATEGORY_LIST_1 = Arrays.asList(CATEGORY_1);
	public static final List<Category> CATEGORY_LIST_2 = Arrays.asList(CATEGORY_1, CATEGORY_2);
	
	public static final Page<Product> PAGE_PRODUCT_LIST = new PageImpl<>(PRODUCT_LIST);
	public static final Page<Product> PAGE_PRODUCT_LIST_1 = new PageImpl<>(PRODUCT_LIST_1);
	public static final Page<Product> PAGE_PRODUCT_LIST_2 = new PageImpl<>(PRODUCT_LIST_2);
	public static final Page<Product> PAGE_PRODUCT_LIST_3 = new PageImpl<>(PRODUCT_LIST_3);
	
	public static final Searchable SEARCHABLE_AVAILABLE = new Searchable(InventoryStockEnum.AVAILABLE, PriceEnum.HIGH, null);
	public static final Searchable SEARCHABLE_LIMITED = new Searchable(InventoryStockEnum.LIMITED, PriceEnum.LOW, null);
	public static final Searchable SEARCHABLE_OOS = new Searchable(InventoryStockEnum.OUT_OF_STOCK, PriceEnum.LOW, "1000,3000");
	public static final Searchable SEARCHABLE_OOS1 = new Searchable(InventoryStockEnum.OUT_OF_STOCK, PriceEnum.HIGH, "1000,3000");
	
	public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.by("name").ascending());
	
	static {
		GENERIC_PRODUCT_RESPONSE = new GenericResponse<>();
		GENERIC_PRODUCT_RESPONSE.header(Status.SUCCESS, HttpStatus.OK, SUCCESS_MSG);
		GENERIC_PRODUCT_RESPONSE.setPayload(NEW_PRODUCT_VO);
	}
	
	static {
		GENERIC_PRODUCT_RESPONSE_LIST = new GenericResponse<>();
		GENERIC_PRODUCT_RESPONSE_LIST.header(Status.SUCCESS, HttpStatus.OK, SUCCESS_MSG);
		GENERIC_PRODUCT_RESPONSE_LIST.setPayload(Arrays.asList(NEW_PRODUCT_VO));
	}
	
	static {
		GENERIC_CATEGORY_RESPONSE = new GenericResponse<>();
		GENERIC_CATEGORY_RESPONSE.header(Status.SUCCESS, HttpStatus.OK, SUCCESS_MSG);
		GENERIC_CATEGORY_RESPONSE.setPayload(CATEGORY_VO);
	}
	
	static {
		GENERIC_CATEGORY_RESPONSE_LIST = new GenericResponse<>();
		GENERIC_CATEGORY_RESPONSE_LIST.header(Status.SUCCESS, HttpStatus.OK, SUCCESS_MSG);
		GENERIC_CATEGORY_RESPONSE_LIST.setPayload(Arrays.asList(CATEGORY_VO));
	}
	
	static {
		GENERIC_CATEGORY_DELETE_RESPONSE = new GenericResponse<>();
		GENERIC_CATEGORY_DELETE_RESPONSE.header(Status.SUCCESS, HttpStatus.OK, "Category Deleted");
		GENERIC_CATEGORY_DELETE_RESPONSE.setPayload(SUCCESS_MSG);
	}
	
	static {
		GENERIC_PRODUCT_DELETE_RESPONSE = new GenericResponse<>();
		GENERIC_PRODUCT_DELETE_RESPONSE.header(Status.SUCCESS, HttpStatus.OK, "Product Deleted");
		GENERIC_PRODUCT_DELETE_RESPONSE.setPayload(SUCCESS_MSG);
	}
	
}
