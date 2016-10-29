package com.test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DHMConcordance {

	private static String fileName = "ConcordanceText.txt";
	private static List<String> sentences = new ArrayList<>();
	private static Map<String, Occurance<Integer, TreeSet<Integer>>> result;
	private static TreeSet<Integer> reference;
	private static Occurance<Integer, TreeSet<Integer>> occ;
	
	public static void main(String[] a) {
		FileReader fr = readFile();
		while (!fr.endOfFile()) {
			sentences.add(fr.readLine());
		}
		result = new TreeMap<String, Occurance<Integer, TreeSet<Integer>>>();

		System.out.println(sentences.size());
		int index = 0;
		for (String s : sentences) {
			addWords(s, ++index);
		}

		displayConcordance();
	}
	
	private static void displayConcordance() {
		System.out.println(result);
	}

	public static void addWords(String line, int lineno) {
		StringTokenizer st = new StringTokenizer(line, " ");
		while (st.hasMoreTokens()) {
			String word = st.nextToken();
			occ = result.get(word);
			if(result.get(word)==null) {
				occ = new Occurance<Integer, TreeSet<Integer>>();
				occ.increment();
				occ.addReference(lineno);
				result.put(word, occ);
			} else {
				occ.increment();
				occ.addReference(lineno);
				result.put(word, occ);
			}
		}
	}

	private static FileReader readFile() {
		FileReader fr = new FileReader();
		fr.readInputFile(FileSystems.getDefault().getPath("", fileName).toAbsolutePath().toString());
		return fr;
	}

	private void readCharData() throws IOException {

	}

}
