package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Password {

    @Column(length = 20, nullable = false)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        this.password = requireNonNull(password, "password");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password userId = (Password) o;
        return Objects.equals(this.password, userId.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
