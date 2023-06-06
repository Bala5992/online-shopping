package com.oshop.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "ATTRIBUTES")
public class ProductAttribute {
	
	@Id
	@SequenceGenerator(name = "gen_attr", sequenceName = "attribute_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "gen_attr", strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "A_NAME")
	private String name;
	
	@Column(name = "A_VALUE")
	private String value;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
}
