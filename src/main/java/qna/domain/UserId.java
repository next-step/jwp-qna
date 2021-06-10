package qna.domain;

import javax.persistence.Column;
import java.util.Objects;

import static java.lang.String.format;

public class UserId {
    public static final int MAXIMUM_LENGTH = 20;

    @Column(length = MAXIMUM_LENGTH, nullable = false, unique = true)
    private String userId;

    protected UserId() {

    }

    public UserId(String userId) {
        validate(userId);

        this.userId = userId;
    }

    private void validate(String userId) {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("사용자 아이디는 null일 수 없습니다.");
        }

        if (userId.isEmpty()) {
            throw new IllegalArgumentException("사용자 아이디는 비어 있을 수 없습니다.");
        }

        if (userId.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("사용자 아이디의 최대 길이는 %d자 입니다.", MAXIMUM_LENGTH));
        }
    }

    @Override
    public String toString() {
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
}
