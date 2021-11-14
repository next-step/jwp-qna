package qna.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static qna.common.QnaConst.MIN_TEXT_LENGTH;

@Embeddable
public class UserId {
    private static final int MAX_LENGTH_USERID = 20;

    @Column(name = "user_id", nullable = false, length = MAX_LENGTH_USERID, unique = true)
    private String userId;

    public UserId(String userId) {
        this.userId = userId;
        validateUserId();
    }

    protected UserId() {
    }

    private void validateUserId() {
        if (Objects.isNull(userId) || isInvalidUserIdLength()) {
            throw new IllegalArgumentException("사용자 아이디의 필수이며 길이는 1이상 20이하여야 합니다.");
        }
    }

    private boolean isInvalidUserIdLength() {
        return userId.length() < MIN_TEXT_LENGTH || userId.length() > MAX_LENGTH_USERID;
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
