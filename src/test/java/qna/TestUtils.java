package qna;

import java.util.Arrays;

public class TestUtils {

	private TestUtils() {}

	public static String createText(int length) {
		char[] chars = new char[length];
		Arrays.fill(chars, 'a');
		return new String(chars);
	}
}
