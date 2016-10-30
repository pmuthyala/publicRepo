package com.test;

public class Occurance<K extends Number, V> {

	private K occCount;
	private StringBuffer sentenceNumber;

	public StringBuffer getSentenceNumber() {
		return sentenceNumber;
	}

	public void appendSentenceNumber(String ref) {
		if (this.sentenceNumber.length() == 0)
			this.sentenceNumber.append(ref);
		else
			this.sentenceNumber.append("," + ref);
	}

	public Occurance(K occCount, StringBuffer reference) {
		this.occCount = occCount;
		this.sentenceNumber = reference;
	}

	public void increment() {
		int i = occCount.intValue();
		i++;
		occCount = (K) new Integer(i);
	}

	@Override
	public String toString() {
		return "{" + occCount + ":" + sentenceNumber.toString() + "}";
	}

}
