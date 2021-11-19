package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserPassword {
    public static final String MAX_USER_PASSWORD_EXCEPTION_MESSAGE = "userPassword 최대입력 길이를 초과하였습니다.";
    public static final int MAX_USER_PASSWORD_LENGTH = 20;

    @Column(nullable = false, length = MAX_USER_PASSWORD_LENGTH)
    private String password;

    protected UserPassword() {
    }

    public UserPassword(String password) {
        validateUserPasswordLength(password);
        this.password = password;
    }

    private void validateUserPasswordLength(String password) {
        if (password.length() > MAX_USER_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(MAX_USER_PASSWORD_EXCEPTION_MESSAGE);
        }
    }

    public void validateMatchPassword(UserPassword otherUserPassword) {
        if (!matchPassword(otherUserPassword.password)) {
            throw new UnAuthorizedException();
        }
    }

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPassword that = (UserPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
