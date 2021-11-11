package qna.utils;

import java.util.Random;

/**
 * packageName : qna.utils
 * fileName : StringUtils
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
public class StringUtils {
    private static final int SMALL_A = 'a';
    private static final int SMALL_Z = 'z';
    private StringUtils() {}

    /***
     * @source : https://www.baeldung.com/java-random-string
     */
    public static String getRandomString(int length) {
        int leftLimit = SMALL_A;
        int rightLimit = SMALL_Z;
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
