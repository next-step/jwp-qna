package qna.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

    @Column(name = "email", length = 50)
    private String value;

    protected Email() {
    }

    public Email(final String email) {
        value = validEmail(email);
    }

    private String validEmail(final String email) {
        return Optional.ofNullable(email)
            .filter(string -> string.trim().length() > 0)
            .filter(string -> string.trim().length() <= 50)
            .map(String::trim)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Email email = (Email)o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
