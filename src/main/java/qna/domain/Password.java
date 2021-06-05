package qna.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    public Password(final String password) {
        this.password = validPassword(password);
    }

    public Password() {
    }

    private String validPassword(final String password) {
        return Optional.ofNullable(password)
            .filter(string -> string.trim().length() > 0)
            .filter(string -> string.trim().length() <= 20)
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
        final Password password1 = (Password)o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
