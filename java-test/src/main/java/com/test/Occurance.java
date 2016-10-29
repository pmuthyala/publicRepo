package com.test;

import java.util.TreeSet;

public class Occurance<K extends Integer, V extends TreeSet<Integer>> {

	private Integer occCount;
	private TreeSet<Integer> sentenceNumber;

	public TreeSet<Integer> getSentenceNumber() {
		return sentenceNumber;
	}

	public void addSentenceNumber(Integer ref) {
		this.sentenceNumber.add(ref);
	}

	public Occurance(K couoccCount, V reference) {
		this.occCount = occCount;
		this.sentenceNumber = reference;
	}

	public Occurance() {
		occCount = 0;
		sentenceNumber = new TreeSet<Integer>();
	}

	public void increment() {
		int i = occCount.intValue();
		i++;
		occCount = new Integer(i);
	}

	@Override
	public String toString() {
		
		return "{" + occCount + ":" + sentenceNumber + "}";
	}
	
	
}
