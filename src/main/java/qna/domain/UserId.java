package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {
    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        this.userId = userId;
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
