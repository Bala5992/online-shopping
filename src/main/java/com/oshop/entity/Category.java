package com.oshop.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "CATEGORIES")
public class Category {

	@Id
	@SequenceGenerator(name = "gen", sequenceName = "category_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "gen", strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "categoryId")
	private List<Product> products;
}
