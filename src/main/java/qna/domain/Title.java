package qna.domain;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    @Column(name = "title", nullable = false, length = 100)
    private String value;

    protected Title() {
    }

    public Title(final String value) {
        this.value = validText(value);
    }

    private String validText(final String text) {
        return Optional.ofNullable(text)
            .filter(string -> string.length() > 0)
            .filter(string -> string.length() <= 100)
            .orElseThrow(IllegalArgumentException::new);
    }

    public String value() {
        return value;
    }
}
