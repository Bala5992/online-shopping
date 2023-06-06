package com.oshop.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oshop.entity.Category;
import com.oshop.enumeration.Status;
import com.oshop.exception.NotFoundException;
import com.oshop.model.CategoryVO;
import com.oshop.model.response.GenericResponse;
import com.oshop.model.response.ResponseError;
import com.oshop.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl {
	
	private final ModelMapper mapper; 
	
	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(ModelMapper mapper, CategoryRepository categoryRepository) {
		this.mapper = mapper;
		this.categoryRepository = categoryRepository;
	}
	
	public GenericResponse<CategoryVO> add(CategoryVO categoryVO) {
		return null;
	}
	
	public GenericResponse<List<CategoryVO>> add(List<CategoryVO> categoryVOs) {
		log.info("[Service] Request entered add all service");
		List<Category> validCategoryList = new LinkedList<>();
		List<ResponseError> errors = new LinkedList<>();
		categoryVOs.stream().forEach(newCategoryVO -> {
			Optional<Category> category = categoryRepository.findByName(newCategoryVO.getName());
			if (category.isPresent()) {
				ResponseError error = new ResponseError(newCategoryVO.getName(), "Category already exists");
				errors.add(error);
				log.debug("Category already exists [%s]", newCategoryVO.getName());
			} else {
				validCategoryList.add(mapper.map(newCategoryVO, Category.class));
			}
		});
		List<Category> successList = categoryRepository.saveAll(validCategoryList);
		GenericResponse<List<CategoryVO>> genericResponse = new GenericResponse<List<CategoryVO>>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, String.format("%d category(s) created", successList.size()));
		genericResponse.setPayload(successList.stream().map(category -> mapper.map(category, CategoryVO.class)).toList());
		genericResponse.setErrors(errors);
		log.info("[Service] [CREATE] Process completed successfully for {} category(s) with {} error(s)", successList.size(), errors.size());
		return genericResponse;
	}

	public GenericResponse<CategoryVO> update(CategoryVO categoryVO) throws NotFoundException {
		log.info("[Service] Request entered update service");
		get(categoryVO.getId());
		categoryRepository.save(mapper.map(categoryVO, Category.class));
		GenericResponse<CategoryVO> genericResponse = new GenericResponse<CategoryVO>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, "Category updated");
		genericResponse.setPayload(categoryVO);
		log.info("[Service] Successfully updated category");
		return genericResponse;
	}

	public GenericResponse<CategoryVO> get(int id) throws NotFoundException {
		log.info("[Service] Request entered get service");
		GenericResponse<CategoryVO> genericResponse = new GenericResponse<CategoryVO>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, "Category found");
		genericResponse.setPayload(mapper.map(categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("No category found with id : %d", id))), CategoryVO.class));
		log.info("[Service] Successfully retrieved category with id {}", id);
		return genericResponse;
	}

	public GenericResponse<List<CategoryVO>> get() {
		log.info("[Service] Request entered get all service");
		GenericResponse<List<CategoryVO>> genericResponse = new GenericResponse<List<CategoryVO>>();
		List<CategoryVO> categoryVOs = categoryRepository.findAll().stream().map(cat -> mapper.map(cat, CategoryVO.class)).toList();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, String.format("%d category(s) found", categoryVOs.size()));
		genericResponse.setPayload(categoryVOs);
		log.info("[Service] Successfully retrieved {} category(s)", categoryVOs.size());
		return genericResponse;
	}

	public GenericResponse<String> delete(int id) throws NotFoundException {
		log.info("[Service] Request entered delete service");
		get(id);
		categoryRepository.deleteById(id);
		GenericResponse<String> genericResponse = new GenericResponse<String>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, "Category deleted");
		log.info("[Service] Successfully deleted category with id {}", id);
		return genericResponse;
	}

}
