package qna.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.lang.String.format;
import static qna.domain.option.LengthLimitation.MAXIMUM_USER_ID_LENGTH;
import static qna.domain.option.LengthLimitation.MINIMUM_USER_ID_LENGTH;

@Embeddable
public class UserId {

    @Column(length = MAXIMUM_USER_ID_LENGTH, nullable = false, unique=true)
    private String userId;

    public UserId() { }

    public UserId(String userId) {
        if (userId == null || userId.length() < MINIMUM_USER_ID_LENGTH || userId.length() > MAXIMUM_USER_ID_LENGTH) {
            throw new IllegalArgumentException(format("유저 아이디는 필수로, 길이는 %d~ %d 입니다.",
                    MINIMUM_USER_ID_LENGTH, MAXIMUM_USER_ID_LENGTH));
        }
        this.userId = userId;
    }

    public String get() {
        return this.userId;
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
