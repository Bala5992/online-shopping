package com.oshop.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oshop.entity.Product;
import com.oshop.enumeration.Status;
import com.oshop.exception.NotFoundException;
import com.oshop.model.NewProductVO;
import com.oshop.model.response.GenericResponse;
import com.oshop.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl {
	
	@Autowired
	private ModelMapper mapper; 
	
	@Autowired
	private ProductRepository productRepository;
	
	public ProductServiceImpl(ModelMapper mapper, ProductRepository productRepository) {
		this.mapper = mapper;
		this.productRepository = productRepository;
	}
	
	public GenericResponse<NewProductVO> add(NewProductVO productVO) {
		log.info("[Service] Request entered Add service");
		Product product = mapper.map(productVO, Product.class);
		Product newProduct = productRepository.save(product);
		GenericResponse<NewProductVO> genericResponse = new GenericResponse<NewProductVO>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, "Product created");
		genericResponse.setPayload(mapper.map(newProduct, NewProductVO.class));
		log.info("[Service] Successfully added product with name [{}]", newProduct.getName());
		return genericResponse;
	}

	public GenericResponse<NewProductVO> update(NewProductVO productVO) throws NotFoundException {
		log.info("[Service] Request entered update service");
		get(productVO.getId());
		productRepository.save(mapper.map(productVO, Product.class));
		GenericResponse<NewProductVO> genericResponse = new GenericResponse<NewProductVO>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, "Product updated");
		genericResponse.setPayload(productVO);
		log.info("[Service] Successfully updated product with name [{}]", productVO.getName());
		return genericResponse;
	}

	public GenericResponse<NewProductVO> get(int id) throws NotFoundException {
		log.info("[Service] Request entered get service");
		GenericResponse<NewProductVO> genericResponse = new GenericResponse<NewProductVO>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, "Product found");
		genericResponse.setPayload(mapper.map(productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("No product found with id : %d", id))), NewProductVO.class));
		log.info("[Service] Successfully retrieved product with id [{}]", id);
		return genericResponse;
	}

	public GenericResponse<List<NewProductVO>> get() {
		log.info("[Service] Request entered get all service");
		GenericResponse<List<NewProductVO>> genericResponse = new GenericResponse<List<NewProductVO>>();
		List<NewProductVO> list = productRepository.findAll().stream().map(cat -> mapper.map(cat, NewProductVO.class)).toList();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, String.format("%d Product(s) found", list.size()));
		genericResponse.setPayload(list);
		log.info("[Service] Successfully retrieved {} product(s)", list.size());
		return genericResponse;
	}

	public GenericResponse<String> delete(int id) throws NotFoundException {
		log.info("[Service] Request entered delete service");
		get(id);
		productRepository.deleteById(id);
		GenericResponse<String> genericResponse = new GenericResponse<String>();
		genericResponse.header(Status.SUCCESS, HttpStatus.OK, "Product deleted");
		log.info("[Service] Successfully deleted product with id [{}]", id);
		return genericResponse;
	}

	public GenericResponse<List<NewProductVO>> add(List<NewProductVO> t) {
//		TODO
		return null;
	}

}
