package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.constant.ErrorCode;

@Embeddable
public class Email {

    private static final int MAX_LENGTH = 50;

    @Column(length = 50)
    private String email;

    protected Email() {
    }

    private Email(String email) {
        validateLength(email);
        this.email = email;
    }

    public static Email of(String email) {
        return new Email(email);
    }

    private void validateLength(String email) {
        if(email.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(ErrorCode.이메일의_길이가_너무_김.getErrorMessage());
        }
    }

    public boolean isEqualEmail(Email email) {
        return this.equals(email);
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
