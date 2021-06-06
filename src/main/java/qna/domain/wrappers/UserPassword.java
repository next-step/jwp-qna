package qna.domain.wrappers;

import qna.util.ValidationUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserPassword {

    private static final int PASSWORD_MAX_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String password;

    public UserPassword() {
    }

    public UserPassword(String password) {
        ValidationUtil.checkValidNullOrEmpty(password);
        ValidationUtil.checkValidTitleLength(password, PASSWORD_MAX_LENGTH);
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPassword userPassword1 = (UserPassword) o;
        return Objects.equals(password, userPassword1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
