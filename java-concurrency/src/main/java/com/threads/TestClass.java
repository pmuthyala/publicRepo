package com.threads;

import java.util.stream.Stream;

public class TestClass {

	public static void main(String []args) {
		System.out.println(Integer.toBinaryString(12345));
		System.out.println(Integer.toBinaryString(12345).replaceAll("0+$", ""));
		String []s = Integer.toBinaryString(12345).replaceAll("0+$", "").split("1+");
		System.out.println(s.length);
		int i = 0;
		while(i < s.length) {
			System.out.println(s[i]);
			i++;
		}
		System.out.println(Stream.of(Integer.toBinaryString(12345).replaceAll("0+$", "").split("1+")).
			filter(a -> a != null).map(String::length).max(Integer::compare).orElse(0));
	}
}
