package com.oshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.oshop.exception.NotFoundException;
import com.oshop.model.CategoryVO;
import com.oshop.model.NewProductVO;
import com.oshop.model.ProductVO;
import com.oshop.model.response.GenericResponse;
import com.oshop.repository.CategoryRepository;
import com.oshop.repository.ProductRepository;
import com.oshop.utils.AppFixtures;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit test cases for shopping service")
public class ShoppingServiceImplTest {

	@InjectMocks
	private ShoppingServiceImpl shoppingServiceImpl;
	
	@Mock
	private ModelMapper mapper; 
	
	@Mock
	private ProductServiceImpl productService; 
	
	@Mock
	private CategoryServiceImpl categoryService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Test
	@DisplayName("Test case to create a product - success")
	public void createProduct_success_test() {
		when(mapper.map(AppFixtures.NEW_PRODUCT_VO, ProductVO.class)).thenReturn(AppFixtures.PRODUCT_VO);
		when(categoryService.get(AppFixtures.CATEGORY_ID)).thenReturn(AppFixtures.GENERIC_CATEGORY_RESPONSE);
		when(productRepository.findByName(AppFixtures.PRODUCT_NAME)).thenReturn(Optional.of(AppFixtures.PRODUCT));
		when(productService.add(AppFixtures.NEW_PRODUCT_VO)).thenReturn(AppFixtures.GENERIC_PRODUCT_RESPONSE);
		
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.createAllProduct(AppFixtures.NEW_PRODUCT_VO_LIST);
		
		verify(categoryService).get(AppFixtures.CATEGORY_ID);
		verify(productRepository).findByName(AppFixtures.PRODUCT_NAME);
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isNotNull();
	}
	
	@Test
	@DisplayName("Test case to create a product no category found")
	public void createProduct_failure_test() {
		when(categoryService.get(AppFixtures.CATEGORY_ID)).thenThrow(NotFoundException.class);
		
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.createAllProduct(AppFixtures.NEW_PRODUCT_VO_LIST);
		verify(categoryService).get(AppFixtures.CATEGORY_ID);
		assertThat(genericResponse.getErrors()).isNotEmpty();
	}
	
	@Test
	@DisplayName("Test case to create all products - success")
	public void createAllProduct_success_test() {
		when(categoryService.get(AppFixtures.CATEGORY_ID)).thenReturn(AppFixtures.GENERIC_CATEGORY_RESPONSE);
		when(productRepository.findByName(AppFixtures.PRODUCT_NAME)).thenReturn(Optional.empty());
		when(mapper.map(AppFixtures.NEW_PRODUCT_VO, ProductVO.class)).thenReturn(AppFixtures.PRODUCT_VO);
		when(productService.add(AppFixtures.NEW_PRODUCT_VO)).thenReturn(AppFixtures.GENERIC_PRODUCT_RESPONSE);
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.createAllProduct(AppFixtures.NEW_PRODUCT_VO_LIST);
		
		verify(categoryService).get(AppFixtures.CATEGORY_ID);
		verify(productRepository).findByName(AppFixtures.PRODUCT_NAME);
		verify(productService).add(AppFixtures.NEW_PRODUCT_VO);
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isEqualTo(String.format("%d Product(s) created", AppFixtures.NEW_PRODUCT_VO_LIST.size()));
	}
	
	@Test
	@DisplayName("Test case to create all products - failure")
	public void createAllProduct_failure_test() {
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.createAllProduct(Collections.emptyList());
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isEqualTo("No Product(s) to add");
	}
	
	@Test
	@DisplayName("Test case to update all products - success")
	public void updateAllProduct_success_test() {
		createProduct_success_test();
		when(mapper.map(AppFixtures.NEW_PRODUCT_VO, ProductVO.class)).thenReturn(AppFixtures.PRODUCT_VO);
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.updateAllProduct(AppFixtures.NEW_PRODUCT_VO_LIST);
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isEqualTo(String.format("%d Product(s) updated", AppFixtures.NEW_PRODUCT_VO_LIST.size()));
	}
	
	@Test
	@DisplayName("Test case to update all products - failure")
	public void updateAllProduct_failure_test() {
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.updateAllProduct(Collections.emptyList());
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isEqualTo("No Product(s) to updated");
	}
	
	@Test
	@DisplayName("Test case to list all products - available - success")
	public void listAllProduct_success_available_test() {
		when(mapper.map(AppFixtures.PRODUCT, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO);
		when(productRepository.findAll(AppFixtures.PAGEABLE)).thenReturn(AppFixtures.PAGE_PRODUCT_LIST);
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.listProducts(AppFixtures.PAGEABLE, AppFixtures.SEARCHABLE_AVAILABLE);
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isNotEqualTo("No product found");
	}
	
	@Test
	@DisplayName("Test case to list all products - limited - success")
	public void listAllProduct_success_limited_test() {
		when(mapper.map(AppFixtures.PRODUCT, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO);
		when(productRepository.findAll(AppFixtures.PAGEABLE)).thenReturn(AppFixtures.PAGE_PRODUCT_LIST);
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.listProducts(AppFixtures.PAGEABLE, AppFixtures.SEARCHABLE_LIMITED);
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isNotEqualTo("No product found");
	}
	
	@Test
	@DisplayName("Test case to list all products - outofstock - success")
	public void listAllProduct_success_oos_test() {
		when(mapper.map(AppFixtures.PRODUCT_1, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO1);
		when(mapper.map(AppFixtures.PRODUCT, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO);
		when(productRepository.findAll(AppFixtures.PAGEABLE)).thenReturn(AppFixtures.PAGE_PRODUCT_LIST_1);
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.listProducts(AppFixtures.PAGEABLE, AppFixtures.SEARCHABLE_OOS);
		genericResponse = shoppingServiceImpl.listProducts(AppFixtures.PAGEABLE, AppFixtures.SEARCHABLE_OOS1);
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isNotEqualTo("No product found");
	}
	
	@Test
	@DisplayName("Test case to list all products - no inventory - success")
	public void listAllProduct_success_no_inventory_test() {
		when(mapper.map(AppFixtures.PRODUCT_3, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO3);
		when(productRepository.findAll(AppFixtures.PAGEABLE)).thenReturn(AppFixtures.PAGE_PRODUCT_LIST_3);
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.listProducts(AppFixtures.PAGEABLE, AppFixtures.SEARCHABLE_OOS);
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isEqualTo("No product found");
	}
	
	@Test
	@DisplayName("Test case to list all products - no price - success")
	public void listAllProduct_success_no_price_test() {
		when(mapper.map(AppFixtures.PRODUCT_2, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO2);
		when(productRepository.findAll(AppFixtures.PAGEABLE)).thenReturn(AppFixtures.PAGE_PRODUCT_LIST_2);
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.listProducts(AppFixtures.PAGEABLE, AppFixtures.SEARCHABLE_OOS);
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isEqualTo("No product found");
	}
	
	@Test
	@DisplayName("Test case to list all categories")
	public void listAllCategories_test() {
		when(categoryService.get()).thenReturn(AppFixtures.GENERIC_CATEGORY_RESPONSE_LIST);
		GenericResponse<List<CategoryVO>> genericResponse = shoppingServiceImpl.listCategories();
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload().size()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("Test case to list all products for category - success")
	public void listProductsForCategory_success_available_test() {
		when(mapper.map(AppFixtures.PRODUCT, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO);
		when(categoryRepository.findByName(AppFixtures.CATEGORY_NAME)).thenReturn(Optional.ofNullable(AppFixtures.CATEGORY_1));
		when(productRepository.findAllByCategoryId(AppFixtures.CATEGORY_ID)).thenReturn(AppFixtures.PRODUCT_LIST);
		
		GenericResponse<List<NewProductVO>> genericResponse = shoppingServiceImpl.listProductsForCategory(AppFixtures.CATEGORY_NAME, AppFixtures.SEARCHABLE_AVAILABLE);
		
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getHeader().getMessage()).isNotEqualTo(String.format("No product found for category : %s", AppFixtures.CATEGORY_NAME) );
	}
	
	@Test
	@DisplayName("Test case to list all products for category - failure")
	public void listProductsForCategory_failure_available_test() {
		when(categoryRepository.findByName(AppFixtures.CATEGORY_NAME)).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, () -> {
			shoppingServiceImpl.listProductsForCategory(AppFixtures.CATEGORY_NAME, AppFixtures.SEARCHABLE_AVAILABLE);
		}, String.format("Category not found : %s", AppFixtures.CATEGORY_NAME));
	}
}
