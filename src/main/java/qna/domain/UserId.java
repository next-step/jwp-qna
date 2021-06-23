package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class UserId {
    private static final Pattern USERID_PATTERN
            = Pattern.compile("^[a-z](\\w{2,19})$");
    public static final String INVALID_USERID_PATTERN = "Invalid UserId pattern";

    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    public UserId(String userId) {
        this.userId = validUserId(userId);
    }

    public UserId() {
    }

    private String validUserId(String userId) {
        if (Objects.isNull(userId) || userId.isEmpty() || !USERID_PATTERN.matcher(userId).find()) {
            throw new IllegalArgumentException(INVALID_USERID_PATTERN);
        }
        return userId;
    }

    public void matchUserId(UserId targetUserId) {
        if (!this.userId.equals(targetUserId.userId)) {
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
}
