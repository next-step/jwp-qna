package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {
    private static final int USER_ID_LENGTH = 20;

    public static UserId of(String userId) {
        return new UserId(userId);
    }

    @Column(nullable = false, length = USER_ID_LENGTH, unique = true)
    private String userId;

    protected UserId() {

    }

    private UserId(String userId) {
        this.userId = userId;
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

    public boolean matchUserId(UserId userId) {
        return this.equals(userId);
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
