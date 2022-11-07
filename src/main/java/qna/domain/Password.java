package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

@Embeddable
public class Password {

    private static final int MAX_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String password;

    protected Password() {
    }

    private Password(String password) {
        validateLength(password);
        this.password = password;
    }

    private void validateLength(String password) {
        if(password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(ErrorCode.비밀번호의_길이가_너무_김.getErrorMessage());
        }
    }

    public static Password of(String password) {
        return new Password(password);
    }

    public void validateMatchPassword(Password password) {
        if(!matchPassword(password)) {
            throw new UnAuthorizedException();
        }
    }

    private boolean matchPassword(Password password) {
        return this.equals(password);
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
