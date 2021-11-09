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
    private StringUtils() {}

    /***
     * @source : https://www.baeldung.com/java-random-string
     */
    public static String getRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
