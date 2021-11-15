package qna.domain;

import static qna.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {
    private static final int PASSWORD_MAX_LENGTH = 20;

    @Column(length = PASSWORD_MAX_LENGTH, nullable = false)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        validateBlank(password);
        validateLength(password);
    }

    private void validateBlank(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(VALIDATE_PASSWORD_MESSAGE.getMessage());
        }
    }

    private void validateLength(String password) {
        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException(VALIDATE_PASSWORD_MESSAGE.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Password password1 = (Password)o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String toString() {
        return "Password{" +
            "password='" + password + '\'' +
            '}';
    }
}
