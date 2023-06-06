package com.oshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	public List<Product> findAllByCategoryId(int categoryId);
	
	public Optional<Product> findByName(String name);
}
