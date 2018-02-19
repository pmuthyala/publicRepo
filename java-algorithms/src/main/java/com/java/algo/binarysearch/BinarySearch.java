package com.java.algo.binarysearch;

public class BinarySearch {

	public static void main(String[] a) {
		int [] arr = { 3, 5, 9, 11, 32, 55, 67, 99};
		int findElement = 32;
		int index = runBinarySearch(arr, findElement, 0, arr.length - 1);
		System.out.println("Item found at Index "+index);
	}
	
	static int runBinarySearch(int []a , int searchItem, int lowIndex, int highIndex) {
		int index = Integer.MAX_VALUE;
		while (lowIndex <= highIndex) {
			int mid = (lowIndex + highIndex) / 2;
			if(a[mid] < searchItem) {
				lowIndex = mid + 1;
			} else if(a[mid] > searchItem) {
				highIndex = mid - 1;
			} else if(a[mid] == searchItem) {
				index = mid;
				break;
			}			
		}
		return index;
	}
}