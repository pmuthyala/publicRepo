package com.jpa.orm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.thoughts.on.java.model.Book;
import org.thoughts.on.java.model.Publisher;

public class TestFirstLevelCacheCriteriaQueryLang {

	private EntityManagerFactory emf;

	public TestFirstLevelCacheCriteriaQueryLang() {
		emf = Persistence.createEntityManagerFactory("my-persistence-unit");
	}

	public static void main(String[] a) {
		TestFirstLevelCacheCriteriaQueryLang m = new TestFirstLevelCacheCriteriaQueryLang();
		EntityManager em = m.getEmf().createEntityManager();
		displayBooks(em);
		System.out.println("With Join >>");
		displayBooksForSpecificPublisher(em);
		em.close();
		m.getEmf().close();
	}

	private static void displayBooksForSpecificPublisher(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		Root<Book> book = cq.from(Book.class);
		cq.select(book);

		// join publisher to get publisher id
		Join<Book, Publisher> joinPub = book.join("publisher");

		Path<String> p = book.get("title");
		Path<Long> pub = joinPub.get("id");

		Predicate bookPred = cb.like(p.as(String.class), "E%");
		Predicate pubPred = cb.equal(pub.as(Long.class), 1);

		Predicate pred = cb.and(bookPred, pubPred);

		cq.where(pred);

		TypedQuery<Book> tq = em.createQuery(cq);
		List<Book> books = tq.getResultList();
		// em.close();
		for (Book b : books) {
			System.out.println("Title >>" + b.getTitle());
			System.out.println("Authors >>" + b.getAuthors());
			System.out.println("Publisher >>" + b.getPublisher().getName());
		}
		//em.close();
	}

	private static void displayBooks(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		Root<Book> book = cq.from(Book.class);
		cq.select(book);
		Path<String> p = book.get("title");
		cq.where(cb.like(p.as(String.class), "E%"));
		TypedQuery<Book> tq = em.createQuery(cq);
		List<Book> books = tq.getResultList();
		// em.close();
		for (Book b : books) {
			System.out.println("Title >>" + b.getTitle());
			System.out.println("Authors >>" + b.getAuthors());
			System.out.println("Publisher >>" + b.getPublisher().getName());
		}
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

}
