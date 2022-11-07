package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.UnAuthorizedException;

@Embeddable
public class UserId {

    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    protected UserId() {
    }

    private UserId(String userId) {
        this.userId = userId;
    }

    public static UserId of(String userId) {
        return new UserId(userId);
    }

    public void validateMatchUserId(UserId userId) {
        if(!matchUserId(userId)) {
            throw new UnAuthorizedException();
        }
    }

    private boolean matchUserId(UserId userId) {
        return this.equals(userId);
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

    @Override
    public String toString() {
        return "UserId{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
