package com.oshop.entity;

import jakarta.persistence.CascadeType;
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
@Table(name = "INVENTORIES")
public class Inventory {

	@Id
	@SequenceGenerator(name = "gen_inventory", sequenceName = "inventory_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "gen_inventory", strategy = GenerationType.AUTO)
	private int id;
	
	private int total;
	
	private int available;
	
	private int reserved;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
}
