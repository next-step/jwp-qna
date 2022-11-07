package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {

    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    public UserId(String userId) {
        this.userId = userId;
    }

    public UserId() {

    }

    public static UserId of(String userId) {
        return new UserId(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId email1 = (UserId) o;
        return Objects.equals(userId, email1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
