package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

    public static final int MAX_PASSWORD_LENGTH = 20;

    @Column(length = MAX_PASSWORD_LENGTH, nullable = false)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        if (password == null) {
            throw new IllegalArgumentException("비밀번호는 반드시 입력되어야 합니다. (최대 길이: " + MAX_PASSWORD_LENGTH + ")");
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 " + MAX_PASSWORD_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + MAX_PASSWORD_LENGTH + ")");
        }
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
