package com.test;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class FileReader {

	private final static char EndOfLine = '\n';
	private final static byte EndOfFile = -1;
	private static Reader fileContent;
	private StringBuffer sb;

	public void readInputFile(String inputFileName) {
		try {
			System.out.println(inputFileName);
			fileContent = Files.newBufferedReader(Paths.get(inputFileName), Charset.defaultCharset());
		} catch (IOException e) {
		}
	}

	public String readLine() {
		try {
			sb = new StringBuffer();
			while (!endOfLine()) {
				byte c = (byte) fileContent.read();
				if (c == EndOfFile)
					break;
				sb.append((char) c);
			}
		} catch (IOException e) {
		}
		return sb.toString();
	}

	protected boolean endOfLine() {
		boolean flag = false;
		try {
			fileContent.mark(1);
			if (fileContent.read() == EndOfLine) {
				flag = true;
			} else {
				flag = false;
				fileContent.reset();
			}
		} catch (IOException e) {
		}
		return flag;
	}

	protected boolean endOfFile() {
		boolean flag = false;
		try {
			fileContent.mark(1);
			if (fileContent.read() == EndOfFile) {
				flag = true;
			} else {
				flag = false;
				fileContent.reset();
			}
		} catch (IOException e) {
		}
		return flag;
	}
}
