package com.jpa.orm;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityDaoImpl {

	// @PersistenceContext(name = "mysql-pu")
	private EntityManager em;

	public EntityDaoImpl() {
		em = Persistence.createEntityManagerFactory("mysql-pu").createEntityManager();
		//em.persist(new A());
	}

	public static void main(String[] a) {
		new EntityDaoImpl();
	}
}
