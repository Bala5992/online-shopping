package com.oshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
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

import com.oshop.entity.Category;
import com.oshop.exception.NotFoundException;
import com.oshop.model.CategoryVO;
import com.oshop.model.response.GenericResponse;
import com.oshop.repository.CategoryRepository;
import com.oshop.utils.AppFixtures;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit test cases for product category")
public class CategoryServiceImplTest {

	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;
	
	@Mock
	private ModelMapper mapper; 
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@BeforeEach
	public void init() {		
	}
	
	@Test
	@DisplayName("Unit test case for adding a category")
	public void add_test() {
		GenericResponse<CategoryVO> genericResponse = categoryServiceImpl.add(AppFixtures.CATEGORY_VO);
		assertThat(genericResponse).isNull();
	}
	
	@Test
	@DisplayName("Unit test case for adding multiple categories")
	public void add_all_test() {
		when(mapper.map(AppFixtures.CATEGORY_VO, Category.class)).thenReturn(AppFixtures.CATEGORY_1);
		when(mapper.map(AppFixtures.CATEGORY_1, CategoryVO.class)).thenReturn(AppFixtures.CATEGORY_VO);
		List<CategoryVO> categoryVOs = Arrays.asList(AppFixtures.CATEGORY_VO);
		List<Category> categorys = Arrays.asList(AppFixtures.CATEGORY_1);
		when(categoryRepository.saveAll(Mockito.anyList())).thenReturn(categorys);
		when(categoryRepository.findByName(AppFixtures.CATEGORY_NAME)).thenReturn(Optional.empty());
		
		GenericResponse<List<CategoryVO>> genericResponse = categoryServiceImpl.add(categoryVOs);
	
		verify(categoryRepository).saveAll(Mockito.anyList());
		verify(categoryRepository).findByName(AppFixtures.CATEGORY_NAME);
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isNotEmpty();
		assertThat(genericResponse.getErrors()).isEmpty();
	}
	
	@Test
	@DisplayName("Unit test case for category already exist")
	public void add_all_already_exist_test() {
		when(categoryRepository.findByName(AppFixtures.CATEGORY_NAME)).thenReturn(Optional.of(AppFixtures.CATEGORY_1));
		
		List<CategoryVO> categoryVOs = Arrays.asList(AppFixtures.CATEGORY_VO);
		when(categoryRepository.saveAll(Mockito.anyList())).thenReturn(Collections.emptyList());
		
		GenericResponse<List<CategoryVO>> genericResponse = categoryServiceImpl.add(categoryVOs);
	
		verify(categoryRepository).saveAll(Mockito.anyList());
		verify(categoryRepository).findByName(AppFixtures.CATEGORY_NAME);
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isEmpty();
		assertThat(genericResponse.getErrors()).isNotEmpty();
	}
	
	@Test
	@DisplayName("Unit test case for updating a category")
	public void update_test() {
		get_success_test();
		when(mapper.map(AppFixtures.CATEGORY_VO, Category.class)).thenReturn(AppFixtures.CATEGORY_1);
		when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(AppFixtures.CATEGORY_1);
		
		GenericResponse<CategoryVO> genericResponse = categoryServiceImpl.update(AppFixtures.CATEGORY_VO);
	
		verify(categoryRepository).save(Mockito.any(Category.class));
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test case for getting a category - success case")
	public void get_success_test() {
		when(mapper.map(AppFixtures.CATEGORY_1, CategoryVO.class)).thenReturn(AppFixtures.CATEGORY_VO);
		when(categoryRepository.findById(Mockito.eq(AppFixtures.CATEGORY_ID))).thenReturn(Optional.ofNullable(AppFixtures.CATEGORY_1));
		
		GenericResponse<CategoryVO> genericResponse = categoryServiceImpl.get(AppFixtures.CATEGORY_ID);
		verify(categoryRepository).findById(AppFixtures.CATEGORY_ID);
		assertThat(genericResponse).isNotNull();
		assertThat(genericResponse.getPayload()).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test case for getting a category - failure case")
	public void get_failure_test() {
		when(categoryRepository.findById(Mockito.eq(AppFixtures.CATEGORY_ID))).thenThrow(NotFoundException.class);
		
		assertThrows(NotFoundException.class, () -> {			
			categoryServiceImpl.get(AppFixtures.CATEGORY_ID);
		}, String.format("No category found with id : %d", AppFixtures.CATEGORY_ID));
		verify(categoryRepository).findById(AppFixtures.CATEGORY_ID);
	}
	
	@Test
	@DisplayName("Unit test case for getting all categories")
	public void get_all_test() {
		List<Category> categorys = Arrays.asList(AppFixtures.CATEGORY_1);
		when(mapper.map(AppFixtures.CATEGORY_1, CategoryVO.class)).thenReturn(AppFixtures.CATEGORY_VO);
		when(categoryRepository.findAll()).thenReturn(categorys);
		
		GenericResponse<List<CategoryVO>> genericResponse = categoryServiceImpl.get();
	
		verify(categoryRepository).findAll();
		assertThat(genericResponse.getPayload().size()).isEqualTo(AppFixtures.CATEGORY_ID);
		assertThat(genericResponse).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test case for deleting a category")
	public void delete_test() {
		get_success_test();
		doNothing().when(categoryRepository).deleteById(AppFixtures.CATEGORY_ID);
		
		GenericResponse<String> genericResponse = categoryServiceImpl.delete(AppFixtures.CATEGORY_ID);
		verify(categoryRepository).deleteById(AppFixtures.CATEGORY_ID);
		assertThat(genericResponse).isNotNull();
	}
	
}
