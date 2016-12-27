package com.jpa.orm;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityDaoImpl {

	private EntityManager em;

	public EntityDaoImpl() {
		em = Persistence.createEntityManagerFactory("mysql-pu").createEntityManager();
	}

	public static void main(String[] a) {
		new EntityDaoImpl();
	}
}
