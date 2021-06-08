package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Password {
    private static final int MAXIMUM_PASSWORD_LENGTH = 20;
    private static final int MINIMUM_PASSWORD_LENGTH = 9;
    private static final Pattern PASSWORD_PATTERN = PatternEnum.PASSWORD.toPattern();
    public static final String INVALID_PASSWORD_MESSAGE = "잘못된 비밀번호 형식입니다.";
    public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "비밀번호는 9~20 자리만 가능합니다.";

    @Column(length = MAXIMUM_PASSWORD_LENGTH, nullable = false)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException(INVALID_PASSWORD_MESSAGE);
        }
        if (password.length() > MAXIMUM_PASSWORD_LENGTH || password.length() < MINIMUM_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(INVALID_PASSWORD_LENGTH_MESSAGE);
        }
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (!matcher.find()) {
            throw new IllegalArgumentException(INVALID_PASSWORD_MESSAGE);
        }
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

    @Override
    public String toString() {
        return "Password{" +
                "password='" + password + '\'' +
                '}';
    }
}
