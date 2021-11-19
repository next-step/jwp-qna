package qna.util;

public class TestUtils {

    public static String createStringLongerThan(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= length; i++) {
            stringBuilder.append("A");
        }
        return stringBuilder.toString();
    }
}
