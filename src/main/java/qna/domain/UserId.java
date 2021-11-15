package qna.domain;

import static qna.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {
    private static final int USER_ID_MAX_LENGTH = 20;

    @Column(unique = true, length = USER_ID_MAX_LENGTH, nullable = false)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        validate(userId);
        this.userId = userId;
    }

    private void validate(String userId) {
        validateBlank(userId);
        validateLength(userId);
    }

    private void validateBlank(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException(VALIDATE_USER_ID_MESSAGE.getMessage());
        }
    }

    private void validateLength(String userId) {
        if (userId.length() > USER_ID_MAX_LENGTH) {
            throw new IllegalArgumentException(VALIDATE_USER_ID_MESSAGE.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserId userId1 = (UserId)o;
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
