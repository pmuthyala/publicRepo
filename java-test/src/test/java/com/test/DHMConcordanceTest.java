package com.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DHMConcordanceTest {

	private static DHMConcordance dc;
	static String fileContent;
	static FileReader fr;

	@BeforeClass
	public static void setUp()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		dc = new DHMConcordance();
		fr = new FileReader();
		fileContent = "Given an arbitrary text document written in English, write a program that will generate a concordance, i.e. an alphabetical list of all word occurrences, labeled with word frequencies. \nBonus: label each word with the sentence numbers in which each occurrence appeared";
		Field fileContentField = fr.getClass().getDeclaredField("fileContent");
		fileContentField.setAccessible(true);
		fileContentField.set(fr,
				new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileContent.getBytes()))));

		Field f = dc.getClass().getDeclaredField("fr");
		f.setAccessible(true);
		f.set(dc, fr);
	}

	@Test
	public void testReadLines()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		dc.readLinesFromFile();
		Field lines = dc.getClass().getDeclaredField("sentences");
		lines.setAccessible(true);
		List<String> sentences = (ArrayList<String>) lines.get(dc);
		Assert.assertEquals("Total Lines match", 2, sentences.size());
	}

	@Test
	public void testWordFormatting() {
		String s = "Bonus:";
		Assert.assertEquals("Word formatted as per display", "bonus", dc.formatString(s));
	}

	@Test
	public void testSingleSentenceConcordance() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Method initMethod = dc.getClass().getDeclaredMethod("initResultMap", null);
		initMethod.setAccessible(true);
		initMethod.invoke(dc, null);

		dc.addWords("Test a concordance of this line, which is a single sentence test", 1);

		Field result = dc.getClass().getDeclaredField("result");
		result.setAccessible(true);
		Map<String, Occurance<Integer, StringBuffer>> c = (Map<String, Occurance<Integer, StringBuffer>>) result
				.get(dc);
		Assert.assertEquals("Concordance of single sentence",
				"{a={2:1,1}, concordance={1:1}, is={1:1}, line={1:1}, of={1:1}, sentence={1:1}, single={1:1}, test={2:1,1}, this={1:1}, which={1:1}}",
				c.toString());
	}

	@Test
	public void testAFileContentOfMultipleLinesConcordance() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Method initMethod = dc.getClass().getDeclaredMethod("initResultMap", null);
		initMethod.setAccessible(true);
		initMethod.invoke(dc, null);

		dc.readLinesFromFile();
		dc.buildConcordance();

		Field result = dc.getClass().getDeclaredField("result");
		result.setAccessible(true);
		Map<String, Occurance<Integer, StringBuffer>> c = (Map<String, Occurance<Integer, StringBuffer>>) result
				.get(dc);

		Assert.assertEquals("Concordance of file content",
				"{a={2:1,1}, all={1:1}, alphabetical={1:1}, an={2:1,1}, appeared={1:2}, arbitrary={1:1}, "
				+ "bonus={1:2}, concordance={1:1}, document={1:1}, each={2:2,2}, english={1:1}, frequencies={1:1}, "
				+ "generate={1:1}, given={1:1}, i.e={1:1}, in={2:1,2}, label={1:2}, labeled={1:1}, list={1:1}, "
				+ "numbers={1:2}, occurrence={1:2}, occurrences={1:1}, of={1:1}, program={1:1}, sentence={1:2}, "
				+ "text={1:1}, that={1:1}, the={1:2}, which={1:2}, will={1:1}, with={2:1,2}, word={3:1,1,2}, "
				+ "write={1:1}, written={1:1}}",
				c.toString());
	}
	
	@AfterClass
	public static void tearDown() {
		dc = null;
		fr = null;
	}
}
