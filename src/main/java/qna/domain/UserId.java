package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class UserId {
    private static final int MAXIMUM_USER_ID_LENGTH = 20;
    private static final Pattern USER_ID_PATTERN = PatternEnum.USER_ID.toPattern();
    public static final String INVALID_USER_ID_MESSAGE = "잘못된 아이디 형식입니다.";

    @Column(length = MAXIMUM_USER_ID_LENGTH, nullable = false, unique = true)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        validate(userId);
        this.userId = userId;
    }

    private void validate(String userId) {
        if (Objects.isNull(userId) || userId.isEmpty() || userId.length() > MAXIMUM_USER_ID_LENGTH) {
            throw new IllegalArgumentException(INVALID_USER_ID_MESSAGE);
        }
        Matcher matcher = USER_ID_PATTERN.matcher(userId);
        if (!matcher.find()) {
            throw new IllegalArgumentException(INVALID_USER_ID_MESSAGE);
        }
    }

    public String getUserId() {
        return userId;
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
