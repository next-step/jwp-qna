package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.exception.IllegalUserIdException;
import qna.domain.exception.UserIdLengthExceedException;

@Embeddable
public class UserId {

    private static final int LENGTH_LIMIT = 20;

    @Column(unique = true, nullable = false, length = LENGTH_LIMIT)
    private String userId;

    protected UserId() {
    }

    private UserId(final String userId) {
        validateNotNullOrEmpty(userId);
        validateLengthNotExceed(userId);

        this.userId = userId;
    }

    private void validateLengthNotExceed(String userId) {
        if (userId.length() > LENGTH_LIMIT) {
            throw new UserIdLengthExceedException(LENGTH_LIMIT);
        }
    }

    public static UserId from(final String userId) {
        return new UserId(userId);
    }

    private static void validateNotNullOrEmpty(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalUserIdException();
        }
    }

    public boolean match(final UserId userId) {
        return equals(userId);
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
