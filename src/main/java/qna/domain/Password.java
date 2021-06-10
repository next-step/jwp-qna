package qna.domain;

import javax.persistence.Column;
import java.util.Objects;

import static java.lang.String.format;

public class Password {
    public static final int MAXIMUM_LENGTH = 20;

    @Column(length = MAXIMUM_LENGTH, nullable = false)
    private String password;

    protected Password() {

    }

    public Password(String password) {
        validate(password);

        this.password = password;
    }

    private void validate(String password) {
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("비밀번호는 null일 수 없습니다.");
        }
        if (password.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("비밀번호의 최대 길이는 %d자 입니다.", MAXIMUM_LENGTH));
        }
    }

    @Override
    public String toString() {
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
