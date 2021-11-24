package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {
    private static final int MAX_LENGTH = 20;
    private static final String INVALID_MESSAGE = "길이 " + MAX_LENGTH + "이하의 비어있지 않은 비밀번호를 입력해주세요.";

    @Column(length = MAX_LENGTH, nullable = false)
    private String password;

    public Password(String password) {
        validatePassword(password);
        this.password = password;
    }

    private void validatePassword(String password) {
        if (password == null || password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(INVALID_MESSAGE);
        }
    }

    protected Password() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Password)) {
            return false;
        }
        Password password1 = (Password)o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
