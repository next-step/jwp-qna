package qna.domain;

import java.util.Optional;

public class Title {

    private final String text;

    public Title(String text) {
        this.text = validText(text);
    }

    private String validText(String text) {
        return Optional.ofNullable(text)
                .filter(string -> string.trim().length() > 0)
                .filter(string -> string.trim().length() < 100)
                .orElseThrow(IllegalArgumentException::new);
    }

    public String text() {
        return text;
    }
}
