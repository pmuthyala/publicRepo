package com.test;

import java.util.TreeSet;

public class Occurance<K extends Integer, V extends TreeSet<Integer>> {

	private Integer occCount;
	private TreeSet<Integer> reference;

	public TreeSet<Integer> getReference() {
		return reference;
	}

	public void addReference(Integer ref) {
		this.reference.add(ref);
	}

	public Occurance(K couoccCount, V reference) {
		this.occCount = occCount;
		this.reference = reference;
	}

	public Occurance() {
		occCount = 0;
		reference = new TreeSet<Integer>();
	}

	public void increment() {
		int i = occCount.intValue();
		i++;
		occCount = new Integer(i);
	}

	@Override
	public String toString() {
		return "Occurance [occCount=" + occCount + ", reference=" + reference + "]";
	}
	
	
}
