package qna.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {
    private static final int MAX_LENGTH_EMAIL = 50;
    private static final String INVALID_EMAIL_LENGTH_MESSAGE = "이메일의 길이는 1이상 50이하여야 합니다.";

    @Column(name = "email", length = MAX_LENGTH_EMAIL)
    private String email;

    public Email(String email) {
        this.email = email;
        validateEmail();
    }

    protected Email() {
    }

    private void validateEmail() {
        if (!Objects.isNull(email) && isInvalidUserIdLength()) {
            throw new IllegalArgumentException(INVALID_EMAIL_LENGTH_MESSAGE);
        }
    }

    private boolean isInvalidUserIdLength() {
        return email.length() > MAX_LENGTH_EMAIL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
