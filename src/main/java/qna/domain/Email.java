package qna.domain;

import static qna.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {
    public static final int EMAIL_MAX_LENGTH = 50;

    @Column(length = EMAIL_MAX_LENGTH)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        if (email != null && email.length() > EMAIL_MAX_LENGTH) {
            throw new IllegalArgumentException(VALIDATE_EMAIL_MESSAGE.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Email email1 = (Email)o;
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
