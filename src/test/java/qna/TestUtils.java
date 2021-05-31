package qna;

import java.util.Arrays;

public class TestUtils {

	private static final char MEANING_LESS_ALPHABET = 'x';

	private TestUtils() {}

	public static String createText(int length) {
		char[] chars = new char[length];
		Arrays.fill(chars, MEANING_LESS_ALPHABET);
		return new String(chars);
	}
}
