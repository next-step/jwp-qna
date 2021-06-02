package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {
    private static final String INVALID_INPUT = "올바르지 않은 입력입니다.";
    private static final int MAXIMUM_USER_ID_LENGTH = 20;

    @Column(nullable = false, unique = true, length = 20)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        this.userId = validate(userId);
    }

    private String validate(String userId) {
        if (Objects.isNull(userId) || userId.isEmpty() || userId.length() > MAXIMUM_USER_ID_LENGTH) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        return userId;
    }

    public String getUserId() {
        return userId;
    }

    public void matchUserIdWith(User target) {
        if (!this.userId.equals(target.getUserId())) {
            throw new UnAuthorizedException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        UserId userId1 = (UserId) o;
        return Objects.equals(getUserId(), userId1.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    @Override
    public String toString() {
        return "UserId{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
