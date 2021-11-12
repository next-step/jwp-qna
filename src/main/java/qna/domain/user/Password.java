package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

    @Column(length = 20, nullable = false)
    private String password;

    public Password(String password) {
        if (password.isEmpty() || password.length() > 20) {
            throw new IllegalArgumentException("Password가 올바르지 않습니다.");
        }
        this.password = password;
    }

    public Password() {

    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
