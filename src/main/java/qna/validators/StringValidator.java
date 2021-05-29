package qna.validators;

import java.util.Objects;

import qna.exceptions.EmptyStringException;
import qna.exceptions.NullStringException;
import qna.exceptions.StringTooLongException;

public final class StringValidator {

    public static void validate(final String text, final int limitLength) {
        if (Objects.isNull(text)) {
            throw new NullStringException("Null 문자열입니다.");
        }

        validateNullable(text, limitLength);
    }

    public static void validateNullable(final String text, final int limitLength) {
        if (text.isEmpty()) {
            throw new EmptyStringException("값이 비었습니다.");
        }

        if (text.length() > limitLength) {
            String message = String.format("'%s'의 길이가 %d를 넘었습니다.", text, limitLength);
            throw new StringTooLongException(message);
        }
    }
}
