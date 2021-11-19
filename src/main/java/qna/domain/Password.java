package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.exception.IllegalPasswordException;
import qna.domain.exception.PasswordLengthExceedException;

@Embeddable
public class Password {
    private static final int LENGTH_LIMIT = 20;

    @Column(nullable = false, length = LENGTH_LIMIT)
    private String password;

    protected Password() {
    }

    private Password(final String password) {
        validateNotNullOrEmpty(password);
        validateLengthNotExceed(password);

        this.password = password;
    }

    private void validateNotNullOrEmpty(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalPasswordException();
        }
    }

    private void validateLengthNotExceed(String password) {
        if(password.length() > LENGTH_LIMIT) {
            throw new PasswordLengthExceedException(LENGTH_LIMIT);
        }
    }

    public static Password from(final String password) {
        return new Password(password);
    }

    public boolean match(final Password password) {
        return equals(password);
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
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
