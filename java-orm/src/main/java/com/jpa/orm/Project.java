package com.jpa.orm;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Project")
public class Project implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer term;
	private Long budget;
	
	public Project(Integer term, Long budget) {
		this.term = term;
		this.budget = budget;
	}
	
	@ManyToMany(mappedBy = "projects")
	private Set<Employee> employees;
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue (strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	//@Column(name = "term")
	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	@Column(name = "budget")
	public Long getBudget() {
		return budget;
	}

	public void setBudget(Long budget) {
		this.budget = budget;
	}

	
}
