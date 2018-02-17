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
		//int element = find5thElementByQuickSelect(arr, 0, n - 1, 5);
		//System.out.println("7th element found is "+element);
		printArray("after end    : ", arr);
		
	}
	
	static int find5thElementByQuickSelect(int []a, int low, int high, int k) {
		if( k >= 0 && k <= high - low + 1) {
			int pos = partition(a, low, high);
			if(pos-low == k) {
				return a[pos];
			}
			if(pos-low > k) {
				return find5thElementByQuickSelect(a, low, pos-1, k);
			}
				
			return find5thElementByQuickSelect(a, pos+1, high, k-pos+low-1);
		}
		return 0;
	}
	
	static void sort(int []a, int low, int high) {
		if(low < high) {
			int pi = partition(a, low, high);
			sort(a, low, pi - 1);
			sort(a, pi+1, high);
		}
	}
	
	private static int partition(int []a, int low, int high) {
		for(int j= low; j < high; j++) {
			if(a[j] < a[high]) {
				int temp = a[low];
				a[low] = a[j];
				a[j] = temp;
				printArray("inside loop  : ", a);
				low++;
			}
		}
		printArray("after loop   : ", a);
		if(a[low] > a[high]) {
			int temp = a[low];
			a[low] = a[high];
			a[high] = temp;
		}
		printArray("after swap   : ", a);
		return low;
	}
	
	private static void printArray(String msg, int [] a) {
		System.out.println();
		System.out.print(msg);
		
		for(int i = 0; i < a.length; i ++)
			System.out.print(" "+a[i]);
	}	
}
