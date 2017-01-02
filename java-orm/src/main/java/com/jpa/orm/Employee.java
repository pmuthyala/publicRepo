package com.jpa.orm;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "A")
public class A implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String s;
	private ZonedDateTime zdt;

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public ZonedDateTime getZdt() {
		return zdt;
	}

	public void setZdt(ZonedDateTime zdt) {
		this.zdt = zdt;
	}

}
