package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {

    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    public UserId(String userId) {
        if (userId.isEmpty() || userId.length() > 20) {
            throw new IllegalArgumentException("UserId가 올바르지 않습니다.");
        }
        this.userId = userId;
    }

    public UserId() {

    }

    public String getId() {
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
