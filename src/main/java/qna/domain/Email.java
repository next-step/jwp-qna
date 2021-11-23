package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {
    private static final int MAX_LENGTH = 50;
    private static final String INVALID_MESSAGE = "길이 " + MAX_LENGTH + "이하의 이메일 입력해주세요.";

    @Column(length = MAX_LENGTH)
    private String email;

    public Email(String email) {
        validateEmail(email);
        this.email = email;
    }

    private void validateEmail(String email) {
        if (email.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(INVALID_MESSAGE);
        }
    }

    protected Email() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        Email email1 = (Email)o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
