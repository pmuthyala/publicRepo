package com.java.algo;

public class FibonacciSeries {

	public static void main (String []args) {
		int a = 1, b = 2;
		generateFibo(a,b);
	}
	
	static void generateFibo(int x, int y) {
		int z = x + y;
		System.out.println(z);
		if(z > 100)
			return;
		int p = y, q = z;
		generateFibo(p, q);
	}
}
