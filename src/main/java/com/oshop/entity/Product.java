package com.oshop.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
@Entity
@Table(name = "PRODUCTS")
public class Product {

	@Id
	@SequenceGenerator(name = "gen_product", sequenceName = "product_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "gen_product", strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private String description;
	
	private String brand;
	
	private int categoryId;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "product")
	private ProductPrice price;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "product")
	private Inventory inventory;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	private List<ProductAttribute> attributes;
}
