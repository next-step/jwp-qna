package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserPassword {
    @Column(nullable = false, length = 20)
    private String password;

    protected UserPassword() {
    }

    public UserPassword(String password) {
        this.password = password;
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
