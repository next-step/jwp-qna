package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {
    public static final String MAX_USER_ID_EXCEPTION_MESSAGE = "userId 최대입력 길이를 초과하였습니다.";
    public static final int MAX_USER_ID_LENGTH = 20;

    @Column(nullable = false, length = MAX_USER_ID_LENGTH, unique = true)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        validateUserIdLength(userId);
        this.userId = userId;
    }

    private void validateUserIdLength(String userId) {
        if (userId.length() > MAX_USER_ID_LENGTH) {
            throw new IllegalArgumentException(MAX_USER_ID_EXCEPTION_MESSAGE);
        }
    }

    public void validateMatchUserId(UserId otherUserId) {
        if (!matchUserId(otherUserId.userId)) {
            throw new UnAuthorizedException();
        }
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId otherUserId = (UserId) o;
        return Objects.equals(userId, otherUserId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
