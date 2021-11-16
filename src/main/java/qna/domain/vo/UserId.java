package qna.domain.vo;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {
    private static final int MAX_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String userId;

    protected UserId() {
    }

    private UserId(String userId) {
        validate(userId);
        this.userId = userId.trim();
    }

    public static UserId of(String userId) {
        return new UserId(userId);
    }

    private void validate(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("이름은 빈값일 수 없습니다.");
        }
        if (userId.trim().length() > MAX_LENGTH) {
            throw new IllegalArgumentException("최대 20자 까지 가능합니다.");
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

        return userId != null ? userId.equals(userId1.userId) : userId1.userId == null;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}
