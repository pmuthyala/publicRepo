package com.jpa.orm;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class ShoppingDaoImpl {

	private EntityManager em;

	public ShoppingDaoImpl() {
		em = Persistence.createEntityManagerFactory("mysql-pu").createEntityManager();
	}
	
	public static void main(String[] a) {
		ShoppingDaoImpl impl = new ShoppingDaoImpl();
	}
}
