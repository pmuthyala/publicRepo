package com.test;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class DHMConcordance {

	private static String fileName = "ConcordanceText.txt";
	private static List<String> sentences = new ArrayList<>();
	private static Map<String, Occurance<Integer, StringBuffer>> result;
	private static TreeSet<Integer> reference;
	private static Occurance<Integer, StringBuffer> occ;

	public static void main(String[] a) {
		FileReader fr = readFile();
		while (!fr.endOfFile()) {
			sentences.add(fr.readLine());
		}
		result = new TreeMap<String, Occurance<Integer, StringBuffer>>(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});
		int index = 0;
		for (String s : sentences) {
			addWords(s, ++index);
		}
		displayConcordance();
	}

	private static void displayConcordance() {
		result.forEach(
				(key, value) -> System.out.println(RowIndex.INSTANCE.getIndex() + ".  " + key + "       " + value));
	}

	private static String formatString(String word) {
		String formattedString;
		formattedString = word.trim().toLowerCase();
		Pattern p = Pattern.compile("[:,.]+$");
		formattedString = formattedString.replaceAll(p.pattern(), "");
		return formattedString;
	}

	public static void addWords(String line, int lineno) {
		StringTokenizer st = new StringTokenizer(line.trim(), " ");
		while (st.hasMoreTokens()) {
			String word = formatString(st.nextToken());
			occ = result.get(word);
			if (occ == null)
				occ = new Occurance<Integer, StringBuffer>(0, new StringBuffer());
			occ.increment();
			occ.appendSentenceNumber("" + lineno);
			result.put(word, occ);
		}
	}

	private static FileReader readFile() {
		FileReader fr = new FileReader();
		fr.readInputFile(FileSystems.getDefault().getPath("", fileName).toAbsolutePath().toString());
		return fr;
	}

	public static class RowIndex {
		private byte b, i;
		private StringBuffer sb;
		public RowIndex() {
			b = 97;
			i = 0;
			sb = new StringBuffer();
			sb.append((char) b);
		}

		public String getIndex() {
			if (b == 123) {
				b = 97;
				sb.setCharAt(i, (char) b);
				sb.append((char) b);
				i++;
			} else {
				for (int k = 0; k <= i; k++)
					sb.setCharAt(k, (char) b);
			}
			b++;
			return sb.toString();
		}
		public static final RowIndex INSTANCE = new RowIndex();
	}
}