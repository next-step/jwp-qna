package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.UnAuthorizedException;

@Embeddable
public class UserAuth {
    public static final String ERROR_MESSAGE_USER_ID_IS_NULL = "아이디가 존재하지 않습니다.";
    public static final String ERROR_MESSAGE_PASSWORD_IS_NULL = "비밀번호가 존재하지 않습니다.";
    public static final String ERROR_MESSAGE_UN_AUTHORIZED = "아이디 또는 비밀번호가 일치하지 않습니다.";
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
            throw new IllegalArgumentException(ERROR_MESSAGE_USER_ID_IS_NULL);
        }

        if (Objects.isNull(password)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_PASSWORD_IS_NULL);
        }
    }

    public void validUpdate(UserAuth loginUserAuth, UserAuth targetUserAuth) {
        if (isNotMatchUserId(loginUserAuth.userId)) {
            throw new UnAuthorizedException(ERROR_MESSAGE_UN_AUTHORIZED);
        }

        if (isNotMatchPassword(targetUserAuth.password)) {
            throw new UnAuthorizedException(ERROR_MESSAGE_UN_AUTHORIZED);
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
