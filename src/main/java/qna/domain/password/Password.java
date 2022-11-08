package qna.domain.password;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

    @Column(length = 20, nullable = false)
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public Password() {

    }

    public static Password of(String password) {
        return new Password(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password email1 = (Password) o;
        return Objects.equals(password, email1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
