package com.jpa.orm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Order")
public class Order extends BaseEntity implements Serializable {

	
	@Column
	private String orderNumber;

	@OneToMany (mappedBy = "order", fetch = FetchType.LAZY)
	private Set<OrderItem> items = new HashSet<OrderItem>();
}
