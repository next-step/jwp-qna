package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Email {

    @Column(length = 50)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        this.email = requireNonNull(email, "email");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email userId = (Email) o;
        return Objects.equals(this.email, userId.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
