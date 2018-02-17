package com.java.algo.quicksort;

/**
 * The most confusing sort
 * @author 
 *
 */
public class QuickSort {

	public static void main(String []a) {
		int [] arr = {23, 11,3,7,1,99,5,2,32};
		int n = arr.length;
		
		printArray("before start : ", arr);
		sort(arr, 0, n - 1);
		printArray("after end    : ", arr);
		
	}
	static void sort(int []a, int low, int high) {
		if(low < high) {
			int pi = partition(a, low, high);
			sort(a, low, pi - 1);
			sort(a, pi+1, high);
		}
	}
	
	private static int partition(int []a, int low, int high) {
		int pivot = a[high];
		int i = (low - 1);
		for(int j= low; j < high; j++) {
			if(a[j] <= pivot) {
				i++;
				int temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				printArray("inside loop  : ", a);
			}
		}
		printArray("after loop   : ", a);
		int temp = a[i + 1];
		a[i + 1] = a[high];
		a[high] = temp;
		printArray("after swap   : ", a);
		return i + 1;
	}
	
	private static void printArray(String msg, int [] a) {
		System.out.println();
		System.out.print(msg);
		
		for(int i = 0; i < a.length; i ++)
			System.out.print(" "+a[i]);
	}	
}
