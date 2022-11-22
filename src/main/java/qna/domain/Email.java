package qna.domain;

import qna.ForbiddenException;
import qna.constant.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {
    private static final int EMAIL_LENGTH = 50;

    public static Email of(String email) {
        return new Email(email);
    }

    @Column(length = EMAIL_LENGTH)
    private String email;

    protected Email() {

    }
    private Email(String email) {
        validateEmail(email);
        this.email = email;
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.이메일_공백.getErrorMessage());
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
