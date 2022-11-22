package qna.domain;

import qna.ForbiddenException;
import qna.constant.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {
    private static final int PASSWORD_LENGTH = 20;

    public static Password of(String password) {
        return new Password(password);
    }

    @Column(nullable = false, length = PASSWORD_LENGTH)
    private String password;

    protected Password() {

    }

    private Password(String password) {
        validatePassword(password);
        this.password = password;
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.패스워드_공백.getErrorMessage());
        }
        if (password.length() > PASSWORD_LENGTH) {
            throw new IllegalArgumentException(ErrorCode.패스워드_길이_초과.getErrorMessage());
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
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    public boolean matchPassword(Password password) {
        return this.equals(password);
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
