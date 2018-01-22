package com.jpa.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Product extends BaseEntity implements Serializable {

	@Column (name = "productName")
	private String name;
}
