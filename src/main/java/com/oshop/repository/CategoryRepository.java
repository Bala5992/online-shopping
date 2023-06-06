package com.oshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oshop.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Category> findByName(String name);

}
