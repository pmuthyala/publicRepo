package com.java.orm;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;

public class PersistenceTest {

	@PersistenceContext
	private EntityManager em = null;

	public PersistenceTest() {
	}
	
	@Test
	public void createSchema() {
		
	}
}
