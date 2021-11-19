package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.exception.EmailLengthExceedException;

@Embeddable
public class Email {
    private static final int LENGTH_LIMIT = 50;

    @Column(length = LENGTH_LIMIT)
    private String email;

    protected Email() {
    }

    private Email(final String email) {
        validateLengthNotExceed(email);

        this.email = email;
    }

    public static Email from(final String email) {
        return new Email(email);
    }

    private void validateLengthNotExceed(String email) {
        if (email.length() > 50) {
            throw new EmailLengthExceedException(LENGTH_LIMIT);
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
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
