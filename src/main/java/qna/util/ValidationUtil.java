package qna.util;

public class ValidationUtil {

    public static final String STING_LENGTH_OUT_BOUND_ERROR_MESSAGE = "입력 가능한 길이는 %s자 이하만 입력 가능합니다.";
    public static final String STRING_NULL_OR_EMPTY_ERROR_MESSAGE = "입력값이 입력되지 않았습니다.";

    public static void checkValidNullOrEmpty(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(STRING_NULL_OR_EMPTY_ERROR_MESSAGE);
        }
    }

    public static void checkValidTitleLength(String value, int length) {
        if (value.length() > length) {
            throw new IllegalArgumentException(String.format(STING_LENGTH_OUT_BOUND_ERROR_MESSAGE, length));
        }
    }
}
