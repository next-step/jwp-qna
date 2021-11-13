package qna.utils;

public class ValidationUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(Object o) {
        return o == null;
    }

}
