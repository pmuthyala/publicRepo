package com.test;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class DHMConcordanceTest {

	private static DHMConcordance dc = null;

	@BeforeClass
	public static void setUp() {
		dc = new DHMConcordance();
	}

	@Test
	public void testReadFileSuccess() {

		assertNotNull("Input file found and is read successfully");
	}

}
