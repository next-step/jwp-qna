package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {
    @Column(unique = true, nullable = false, length = 20)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        this.userId = userId;
    }

    public void validateMatchUserId(UserId targetUserId) {
        if (!this.equals(targetUserId)) {
            throw new UnAuthorizedException();
        }
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

    @Override
    public String toString() {
        return "UserId{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
