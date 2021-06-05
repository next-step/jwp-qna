package qna.util;

import java.util.Optional;

public class StringValidateUtils {

    public static String validate(final String value, final Integer length) {
        return Optional.ofNullable(value)
            .filter(string -> string.trim().length() > 0)
            .filter(string -> string.trim().length() <= length)
            .map(String::trim)
            .orElseThrow(IllegalArgumentException::new);
    }
}
