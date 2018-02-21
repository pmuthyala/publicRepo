package com.collections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectorsExample {

	public static void main (String []args) {
		List<String> givenList = Arrays.asList("a", "bb", "ccc", "dd", "a");
		
		//Collect them into list
		List<String> result = givenList.stream().collect(Collectors.toList());
		System.out.println("result value "+result);
		
		//Collect them into set - duplicate elements will be removed.
		Set<String> setresult = givenList.stream().collect(Collectors.toSet());
		System.out.println("set result value "+setresult);


		//Collect them into map, 3rd parameter to avoid collisions
		Map<String, Integer> mapresult = givenList.stream().collect(Collectors.toMap(Function.identity(), String::length, (i1, i2) -> i1 ));
		System.out.println("map result value "+mapresult);

		//collectingandThen
		//List<String> collectresult = givenList.stream().collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
		
		//functional interfaces
		Map<String, Integer> nameMap = new HashMap<>();
		Integer value = nameMap.computeIfAbsent("John", s -> s.length());
		System.out.println("Integer value is "+value.intValue());
		
		//function interface
		int []array = {43459999, 239999323, 599923234, 743999998, 14342, 84449, 32423233,55622};
		short []sarray =transformArray(array, new IntToShortFunction() {
			@Override
			public short applyAsShort(int i) {
				return (short)i;
			}
		});
		System.out.print("transformed array is ");
		for(int i = 0; i < sarray.length; i++)
		System.out.print(" "+ sarray[i]);
		
		//supplier functional interface
		
		//consumer functional interface
		List<String> names = Arrays.asList("John", "Freddy", "Sam");
		names.forEach(name -> System.out.println("Hello " + name));
		
		//predicate
		
		//operator
		
		
	}
	
	static short[] transformArray(int []a, IntToShortFunction function) {
		short []transformedArray = new short[a.length];
		for(int i = 0; i < transformedArray.length; i++) {
			transformedArray[i] = function.applyAsShort(a[i]);
		}
		return transformedArray;
	}
	
	
}
