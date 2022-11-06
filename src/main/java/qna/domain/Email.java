package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.message.ExceptionMessage;

@Embeddable
public class Email {
    private static final int MAX_EMAIL_LENGTH = 50;
    @Column(length = 50)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        this.email = email;
    }

    public static Email from(String email) {
        validateLength(email);
        return new Email(email);
    }

    private static void validateLength(String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException(String.format(ExceptionMessage.INVALID_EMAIL_LENGTH, email.length()));
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

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                '}';
    }
}
