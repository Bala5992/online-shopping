package com.oshop.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.oshop.utils.AppFixtures;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Unit test cases for Product Repository Layer")
public class ProductRepositoryTest {

	@Autowired 
	private DataSource dataSource;
	
	@Autowired 
	private ProductRepository productRepository;
	
	@Autowired 
	private CategoryRepository categoryRepository;
	
	@BeforeAll
	public void setup() {
		categoryRepository.save(AppFixtures.CATEGORY_1);
		productRepository.save(AppFixtures.PRODUCT);
	}
	
	@Test
	@DisplayName("Unit test cases for Datasource config")
	void injectedComponentsAreNotNull(){
		assertThat(dataSource).isNotNull();
	    assertThat(productRepository).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test cases to add a product")
	public void add_product_test() {
		assertThat(productRepository.findAll()).isNotEmpty();
	}
	
	@Test
	@DisplayName("Unit test cases to add all product(s)")
	public void add_all_category_test() {
		productRepository.saveAll(AppFixtures.PRODUCT_LIST_1);
		
		assertThat(productRepository.findAll()).isNotEmpty();
	}
	
	@Test
	@DisplayName("Unit test cases to find a product by id")
	public void get_product_test() {
		assertThat(productRepository.findById(AppFixtures.PRODUCT_ID)).isNotNull();
	}
	
	@Test
	@DisplayName("Unit test cases to delete a product")
	public void delete_product_test() {
		productRepository.deleteById(AppFixtures.PRODUCT_ID);
		
		assertThat(productRepository.findById(AppFixtures.PRODUCT_ID)).isEmpty();
	}
}
