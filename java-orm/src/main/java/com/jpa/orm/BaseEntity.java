package com.jpa.orm;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	@Id
	@Column(name="id", updatable=false, nullable = false)
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id = null;
	
	@Version
	@Column(name="version")
	private int version =0;
	
	@Column(name = "createdBy")
	private String createdBy;
	
	@Column(name = "modifiedBy")
	private String modifiedBy;
	
	@Column(name = "createdDate")
	private ZonedDateTime createdDate;
	
	@Column(name = "modifiedDate")
	private ZonedDateTime modifiedDate;
	
	
	
	
	
}
