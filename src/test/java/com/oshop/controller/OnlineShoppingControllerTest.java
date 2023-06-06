package com.oshop.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oshop.enumeration.AuthHeaderEnum;
import com.oshop.enumeration.InventoryStockEnum;
import com.oshop.enumeration.PriceEnum;
import com.oshop.exception.NotFoundException;
import com.oshop.model.NewProductVO;
import com.oshop.model.request.Searchable;
import com.oshop.service.impl.CategoryServiceImpl;
import com.oshop.service.impl.ProductServiceImpl;
import com.oshop.service.impl.ShoppingServiceImpl;
import com.oshop.utils.AppConstants;
import com.oshop.utils.AppFixtures;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OnlineShoppingController.class)
@ActiveProfiles("dev")
@DisplayName("Shopping unit test cases for web layer")
@TestInstance(Lifecycle.PER_CLASS)
public class OnlineShoppingControllerTest {

	@MockBean
	private ShoppingServiceImpl adminService;
	
	@MockBean
	private CategoryServiceImpl categoryService;
	
	@MockBean
	private ProductServiceImpl productService;
	
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mvc;
	
	@BeforeAll
	public void init() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}
	
	@Test
	@DisplayName("Unit test case when no auth header is present")
	public void auth_header_success() throws Exception {
	   String uri = "/api/products";
	   
	   String inputJson = objectMapper.writeValueAsString(AppFixtures.NEW_PRODUCT_VO);
	   
	    mvc.perform(MockMvcRequestBuilders.post(uri)
			   .contentType(MediaType.APPLICATION_JSON_VALUE)
			   .content(inputJson))
			   .andExpect(status().isForbidden())
			   .andExpect(content().string(containsString(AppFixtures.AUTH_HEADER_NOT_AVAILABLE)));
	}
	
	@Test
	@DisplayName("Unit test case to create new products - failure")
	public void createProduct_invalid_json_test() throws Exception {
	   String uri = "/api/products";
	   mvc.perform(MockMvcRequestBuilders.post(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(AppFixtures.PAYLOAD_INVALID_JSON)).andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Unit test case to create new products - success")
	public void createProduct_test() throws Exception {
	   String uri = "/api/products";
	   when(adminService.createAllProduct(anyList())).thenReturn(AppFixtures.GENERIC_PRODUCT_RESPONSE_LIST);
	   
	   String inputJson = objectMapper.writeValueAsString(AppFixtures.NEW_PRODUCT_VO_LIST);
	   
	   mvc.perform(MockMvcRequestBuilders.post(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(inputJson)).andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is(AppFixtures.SUCCESS_MSG)));
	   
	   verify(adminService).createAllProduct(anyList());
	}
	
	@Test
	@DisplayName("Unit test case to create new products - failure")
	public void createProduct_fieldvalidation_400_test() throws Exception {
	   String uri = "/api/products";
	   
	   List<NewProductVO> list = new ArrayList<>();
	   list.add(AppFixtures.NEW_PRODUCT_VO4);
	   String inputJson = objectMapper.writeValueAsString(list);
	   
	   mvc.perform(MockMvcRequestBuilders.post(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(inputJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.header.message", is(AppFixtures.FIELD_VALIDATIONS_MESSAGE)));
	}
	
	@Test
	@DisplayName("Unit test case to update products")
	public void updateProduct_test() throws Exception {
	   String uri = "/api/products";
	   when(adminService.updateAllProduct(anyList())).thenReturn(AppFixtures.GENERIC_PRODUCT_RESPONSE_LIST);
	   
	   String inputJson = objectMapper.writeValueAsString(AppFixtures.NEW_PRODUCT_VO_LIST);
	   
	   mvc.perform(MockMvcRequestBuilders.put(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(inputJson)).andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is(AppFixtures.SUCCESS_MSG)));
	   verify(adminService).updateAllProduct(anyList());
	}
	
	@Test
	@DisplayName("Unit test case to create new category")
	public void createCategory_test() throws Exception {
	   String uri = "/api/categories";
	   when(categoryService.add(anyList())).thenReturn(AppFixtures.GENERIC_CATEGORY_RESPONSE_LIST);
	   
	   String inputJson = objectMapper.writeValueAsString(AppFixtures.CATEGORY_VO_LIST);
	   
	   mvc.perform(MockMvcRequestBuilders.post(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(inputJson)).andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is(AppFixtures.SUCCESS_MSG)));
	   verify(categoryService).add(anyList());
	}
	
	@Test
	@DisplayName("Unit test case to list all products")
	public void listProducts_test() throws Exception {
	   String uri = "/api/products";
	   when(adminService.listProducts(any(Pageable.class), any(Searchable.class))).thenReturn(AppFixtures.GENERIC_PRODUCT_RESPONSE_LIST);
	   
	   mvc.perform(MockMvcRequestBuilders.get(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN)
			   .param("page", "1")
			   .param("size", "8")
			   .param("inventory", InventoryStockEnum.AVAILABLE.name())
			   .param("price", PriceEnum.LOW.name()))
	   	.andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is(AppFixtures.SUCCESS_MSG)));
	   verify(adminService).listProducts(any(Pageable.class), any(Searchable.class));
	}
	
	@Test
	@DisplayName("Unit test case to get a product by id - success")
	public void getProduct_success_test() throws Exception {
	   String uri = "/api/products/" + AppFixtures.PRODUCT_ID;
	   when(productService.get(AppFixtures.PRODUCT_ID)).thenReturn(AppFixtures.GENERIC_PRODUCT_RESPONSE);
	   
	   mvc.perform(MockMvcRequestBuilders.get(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN))
			   .andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is(AppFixtures.SUCCESS_MSG)));
	   
	   verify(productService).get(AppFixtures.PRODUCT_ID);
	}
	
	@Test
	@DisplayName("Unit test case to get a product by id - failure")
	public void getProduct_failure_404_test() throws Exception {
	   String uri = "/api/products/" + Integer.MAX_VALUE;
	   when(productService.get(Integer.MAX_VALUE)).thenThrow(new NotFoundException(String.format("No product found with id : %d", Integer.MAX_VALUE)));
	   
	   mvc.perform(MockMvcRequestBuilders.get(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN))
			   .andExpect(status().isNotFound()).andExpect(jsonPath("$.header.message", is(String.format("No product found with id : %d", Integer.MAX_VALUE))));
	   verify(productService).get(Integer.MAX_VALUE);
	}
	
	@Test
	@DisplayName("Unit test case to list all categories")
	public void listCategories_test() throws Exception {
	   String uri = "/api/products/categories";
	   when(adminService.listCategories()).thenReturn(AppFixtures.GENERIC_CATEGORY_RESPONSE_LIST);
	   
	   mvc.perform(MockMvcRequestBuilders.get(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN))
			   .andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is(AppFixtures.SUCCESS_MSG)));
	   verify(adminService).listCategories();
	}
	
	@Test
	@DisplayName("Unit test case to list all products for given category")
	public void listProductsForCategory_test() throws Exception {
	   String uri = "/api/products/category";
	   when(adminService.listProductsForCategory(anyString(), any(Searchable.class))).thenReturn(AppFixtures.GENERIC_PRODUCT_RESPONSE_LIST);
	   
	   mvc.perform(MockMvcRequestBuilders.get(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN)
			   .param("name", AppFixtures.CATEGORY_NAME)
			   .param("inventory", InventoryStockEnum.AVAILABLE.name())
			   .param("price", PriceEnum.LOW.name()))
			   .andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is(AppFixtures.SUCCESS_MSG)));
	   verify(adminService).listProductsForCategory(anyString(), any(Searchable.class));
	}
	
	@Test
	@DisplayName("Unit test case to delete a category")
	public void deleteCategory_test() throws Exception {
	   String uri = "/api/categories/"+AppFixtures.CATEGORY_ID;
	   when(categoryService.delete(AppFixtures.CATEGORY_ID)).thenReturn(AppFixtures.GENERIC_CATEGORY_DELETE_RESPONSE);
	   
	   mvc.perform(MockMvcRequestBuilders.delete(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN))
			   .andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is("Category Deleted")));
	   verify(categoryService).delete(AppFixtures.CATEGORY_ID);
	}
	
	@Test
	@DisplayName("Unit test case to delete a product")
	public void deleteProduct_test() throws Exception {
	   String uri = "/api/products/"+AppFixtures.PRODUCT_ID;
	   when(productService.delete(AppFixtures.PRODUCT_ID)).thenReturn(AppFixtures.GENERIC_PRODUCT_DELETE_RESPONSE);
	   
	   mvc.perform(MockMvcRequestBuilders.delete(uri)
			   .header(AppConstants.AUTH_PARAM_NAME, AuthHeaderEnum.ADMIN))
	   	.andExpect(status().isOk()).andExpect(jsonPath("$.header.message", is("Product Deleted")));
	   verify(productService).delete(AppFixtures.PRODUCT_ID);
	}
}
