package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    public Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override public int hashCode() {
        return Objects.hash(password);
    }
}
