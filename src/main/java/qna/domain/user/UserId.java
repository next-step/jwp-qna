package qna.domain.user;

import qna.exception.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {

    @Column(length = 20, nullable = false, updatable = false, unique = true)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        this.userId = userId;
    }

    public void isMatch(UserId targetUserId) {
        if (!this.equals(targetUserId)) {
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
        UserId userId1 = (UserId) o;
        return Objects.equals(userId, userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
