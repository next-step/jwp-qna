package qna.domain;

import javax.persistence.*;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

@Embeddable
public class UserId {

    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        this.userId = requireNonNull(userId, "userId");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(this.userId, userId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
