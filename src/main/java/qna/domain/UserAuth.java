package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.UnAuthorizedException;
import qna.common.ErrorMessage;

@Embeddable
public class UserAuth {
    @Column(name = "user_id", unique = true, nullable = false, length = 20)
    private String userId;

    @Column(nullable = false, length = 20)
    private String password;

    protected UserAuth() {
    }

    public UserAuth(String userId, String password) {
        validUserAuth(userId, password);

        this.userId = userId;
        this.password = password;
    }

    private void validUserAuth(String userId, String password) {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException(ErrorMessage.ID_DOES_NOT_EXIST);
        }

        if (Objects.isNull(password)) {
            throw new IllegalArgumentException(ErrorMessage.PASSWORD_DOES_NOT_EXIST);
        }
    }

    public void validUpdate(UserAuth loginUserAuth, UserAuth targetUserAuth) {
        if (isNotMatchUserId(loginUserAuth.userId)) {
            throw new UnAuthorizedException(ErrorMessage.ID_OR_PASSWORD_NOT_MATCH);
        }

        if (isNotMatchPassword(targetUserAuth.password)) {
            throw new UnAuthorizedException(ErrorMessage.ID_OR_PASSWORD_NOT_MATCH);
        }
    }

    private boolean isNotMatchUserId(String userId) {
        return !this.userId.equals(userId);
    }

    private boolean isNotMatchPassword(String password) {
        return !this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAuth userAuth = (UserAuth) o;
        return Objects.equals(userId, userAuth.userId) && Objects.equals(password, userAuth.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password);
    }
}
