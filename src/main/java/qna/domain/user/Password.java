package qna.domain.user;

import qna.exception.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

    @Column(length = 20, nullable = false)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    public void isMatch(Password targetPassword) {
        if (!this.equals(targetPassword)) {
            throw new UnAuthorizedException("사용자 정보가 일치하지 않습니다.");
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

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
