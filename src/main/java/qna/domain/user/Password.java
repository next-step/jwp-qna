package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static qna.constant.Message.NOT_VALID_EMPTY;

@Embeddable
public class Password {
    @Column(name = "password", nullable = false, length= 20)
    private String password;

    public Password(String password) {
        validatePassword(password);
        this.password = password;
    }

    public Password() {
    }

    public static Password of(String password) {
        return new Password(password);
    }

    private void validatePassword(String password) {
        if(password.isEmpty()) {
            throw new IllegalArgumentException(NOT_VALID_EMPTY + "[비밀번호]");
        }
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
