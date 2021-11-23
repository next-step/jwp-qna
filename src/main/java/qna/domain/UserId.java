package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {
    private static final int MAX_LENGTH = 20;
    private static final String INVALID_MESSAGE = "길이 " + MAX_LENGTH + "이하의 비어있지 않은 유저ID를 입력해주세요.";

    @Column(length = MAX_LENGTH, nullable = false, unique = true)
    private String userId;

    public UserId(String userId) {
        validateUserId(userId);
        this.userId = userId;
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.length() > MAX_LENGTH)
            throw new IllegalArgumentException(INVALID_MESSAGE);
    }

    protected UserId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserId)) {
            return false;
        }
        UserId userId1 = (UserId)o;
        return Objects.equals(userId, userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
