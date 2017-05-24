package com.jpa.orm;

import java.time.ZonedDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class EntityDaoImpl {

	// @PersistenceContext(name = "mysql-pu")
	private EntityManager em;

	public EntityDaoImpl() {
		em = Persistence.createEntityManagerFactory("mysql-pu").createEntityManager();
		loadData();
	}

	public void loadData() {
		Department d = null;
		Employee e = null;
		Project p = null;
		em.getTransaction().begin();
		d = new Department("IT");
		em.persist(d);
		e = new Employee("Emp1", ZonedDateTime.now(), d.getId());
		em.persist(e);
		e = new Employee("Emp2", ZonedDateTime.now(), d.getId());
		em.persist(e);
		e = new Employee("Emp3", ZonedDateTime.now(), d.getId());
		em.persist(e);
		d = new Department("HR");
		em.persist(d);
		e = new Employee("Emp4", ZonedDateTime.now(), d.getId());
		em.persist(e);
		e = new Employee("Emp5", ZonedDateTime.now(), d.getId());
		em.persist(e);
		e = new Employee("Emp6", ZonedDateTime.now(), d.getId());
		em.persist(e);
		e = new Employee("Emp7", ZonedDateTime.now(), d.getId());
		em.persist(e);
		p = new Project(5, 100000L);
		em.persist(p);
		p = new Project(7, 200000L);
		em.persist(p);
		
		System.out.println(d.getId());
		
		em.getTransaction().commit();
		System.out.println("Again>>"+d.getId());
		//em.persist(new Employee());
	}
	public static void main(String[] a) {
		//Long x = 1667l, y = 1667l;
		//if(x.longValue()==y.longValue())
			//System.out.println("Same");
		EntityDaoImpl impl = new EntityDaoImpl();
		impl.readData();
	}
	
	public void readData() {
		Employee e = em.find(Employee.class, 7l);
		System.out.println(e.getName());
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery(Employee.class);
		Root r = cq.from(Employee.class);
		cq.select(r).where(cb.equal(r.get("dept").get("name"), "HR"));

	}
}
