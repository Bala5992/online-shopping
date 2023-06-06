package com.oshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.oshop.entity.Product;
import com.oshop.exception.NotFoundException;
import com.oshop.model.NewProductVO;
import com.oshop.model.response.GenericResponse;
import com.oshop.repository.ProductRepository;
import com.oshop.utils.AppFixtures;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit test cases for products")
public class ProductServiceImplTest {

	@InjectMocks
	private ProductServiceImpl productServiceImpl;
	
	@Mock
	private ModelMapper mapper; 
	
	@Mock
	private ProductRepository productRepository;
	
	@BeforeEach
	public void init() {		
	}
	
	@Test
	@DisplayName("Unit test case for adding a product")
	public void add_test() {
		when(mapper.map(AppFixtures.NEW_PRODUCT_VO, Product.class)).thenReturn(AppFixtures.PRODUCT);
		when(mapper.map(AppFixtures.PRODUCT, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO);
		when(productRepository.save(Mockito.any(Product.class))).thenReturn(AppFixtures.PRODUCT);
		
		GenericResponse<NewProductVO> genericResponse = productServiceImpl.add(AppFixtures.NEW_PRODUCT_VO);
	
		verify(productRepository).save(AppFixtures.PRODUCT);
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test case for adding multiple products")
	public void add_all_test() {
		List<NewProductVO> productVOs = Arrays.asList(AppFixtures.NEW_PRODUCT_VO);
		GenericResponse<List<NewProductVO>> genericResponse = productServiceImpl.add(productVOs);
		assertThat(genericResponse).isNull();
	}
	
	@Test
	@DisplayName("Unit test case for updating a product")
	public void update_test() {
		get_success_test();
		when(mapper.map(AppFixtures.NEW_PRODUCT_VO, Product.class)).thenReturn(AppFixtures.PRODUCT);
		when(productRepository.save(Mockito.any(Product.class))).thenReturn(AppFixtures.PRODUCT);
		
		GenericResponse<NewProductVO> genericResponse = productServiceImpl.update(AppFixtures.NEW_PRODUCT_VO);
	
		verify(productRepository).save(Mockito.any(Product.class));
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test case for getting a product - success case")
	public void get_success_test() {
		when(mapper.map(AppFixtures.PRODUCT, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO);
		when(productRepository.findById(Mockito.eq(AppFixtures.PRODUCT_ID))).thenReturn(Optional.ofNullable(AppFixtures.PRODUCT));
		
		GenericResponse<NewProductVO> genericResponse = productServiceImpl.get(AppFixtures.PRODUCT_ID);
		verify(productRepository).findById(AppFixtures.PRODUCT_ID);
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test case for getting a product - failure case")
	public void get_failure_test() {
		when(productRepository.findById(Mockito.eq(AppFixtures.PRODUCT_ID))).thenThrow(NotFoundException.class);
		assertThrows(NotFoundException.class, () -> {			
			productServiceImpl.get(AppFixtures.PRODUCT_ID);
		}, String.format("No product found with id : %d", AppFixtures.PRODUCT_ID));
		verify(productRepository).findById(AppFixtures.PRODUCT_ID);
	}
	
	@Test
	@DisplayName("Unit test case for getting all categories")
	public void get_all_test() {
		List<Product> products = Arrays.asList(AppFixtures.PRODUCT);
		when(mapper.map(AppFixtures.PRODUCT, NewProductVO.class)).thenReturn(AppFixtures.NEW_PRODUCT_VO);
		when(productRepository.findAll()).thenReturn(products);
		
		GenericResponse<List<NewProductVO>> genericResponse = productServiceImpl.get();
	
		verify(productRepository).findAll();
		assertThat(genericResponse.getPayload().size()).isEqualTo(AppFixtures.PRODUCT_ID);
		assertThat(genericResponse).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test case for deleting a product")
	public void delete_test() {
		get_success_test();
		doNothing().when(productRepository).deleteById(AppFixtures.PRODUCT_ID);
		
		GenericResponse<String> genericResponse = productServiceImpl.delete(AppFixtures.PRODUCT_ID);
		verify(productRepository).deleteById(AppFixtures.PRODUCT_ID);
		assertThat(genericResponse).isNotNull();
	}
	
}
