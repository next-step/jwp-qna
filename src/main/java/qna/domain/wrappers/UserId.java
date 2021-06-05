package qna.domain.wrappers;

import qna.util.ValidationUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {

    private static final int USER_ID_MAX_LENGTH = 20;

    @Column(name = "user_id", nullable = false, length = USER_ID_MAX_LENGTH)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        ValidationUtil.checkValidNullOrEmpty(userId);
        ValidationUtil.checkValidTitleLength(userId, USER_ID_MAX_LENGTH);
        this.userId = userId;
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
