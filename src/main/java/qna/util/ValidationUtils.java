package qna.util;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static void checkEmpty(String target) {
        if (target.isEmpty()) {
            throw new IllegalArgumentException("값이 입력되지 않았습니다.");
        }
    }

    public static void checkLength(String target, int maxLength) {
        if (target.length() > maxLength) {
            throw new IllegalArgumentException(String.format("길이가 %d자를 초과했습니다.", maxLength));
        }
    }

}
