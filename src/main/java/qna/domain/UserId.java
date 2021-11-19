package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {

    public static final int MAX_USER_ID_LENGTH = 20;

    @Column(length = MAX_USER_ID_LENGTH, nullable = false, unique = true)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 반드시 입력되어야 합니다. (최대 길이: " + MAX_USER_ID_LENGTH + ")");
        }

        if (userId.length() > MAX_USER_ID_LENGTH) {
            throw new IllegalArgumentException("사용자 ID는 " + MAX_USER_ID_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + MAX_USER_ID_LENGTH + ")");
        }
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId1 = (UserId) o;
        return Objects.equals(userId, userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
