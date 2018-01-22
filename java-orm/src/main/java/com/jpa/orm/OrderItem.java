package com.jpa.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem extends BaseEntity implements Serializable {

	@Column(name = "quantity")
	private int quantity;
	
	@ManyToOne
	private Order order;
	
	@ManyToOne (fetch = FetchType.LAZY)
	private Product product;
}
