package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.message.ExceptionMessage;

@Embeddable
public class Password {
    private static final int MAX_PASSWORD_LENGTH = 20;
    @Column(nullable = false, length = 20)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    public static Password from(String password) {
        validateNotNull(password);
        validateLength(password);
        return new Password(password);
    }

    private static void validateNotNull(String password) {
        if (password == null) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PASSWORD);
        }
    }

    private static void validateLength(String password) {
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(ExceptionMessage.INVALID_PASSWORD_LENGTH, password.length())
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password1 = (Password) o;
        return password.equals(password1.password);
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
