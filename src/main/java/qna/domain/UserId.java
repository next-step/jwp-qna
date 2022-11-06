package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.message.ExceptionMessage;

@Embeddable
public class UserId {

    private static final int MAX_USER_ID_LENGTH = 20;
    @Column(unique = true, nullable = false, length = 20)
    private String userId;

    protected UserId() {
    }

    private UserId(String userId) {
        this.userId = userId;
    }

    public static UserId from(String userId) {
        validateNotNull(userId);
        validateLength(userId);
        return new UserId(userId);
    }

    private static void validateNotNull(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_USER_ID);
        }
    }

    private static void validateLength(String userId) {
        if (userId.length() > MAX_USER_ID_LENGTH) {
            throw new IllegalArgumentException(String.format(ExceptionMessage.INVALID_USER_ID_LENGTH, userId.length()));
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
        return userId.equals(userId1.userId);
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
