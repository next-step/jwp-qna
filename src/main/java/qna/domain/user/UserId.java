package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static qna.constant.Message.NOT_VALID_EMAIL;
import static qna.constant.Message.NOT_VALID_EMPTY;

@Embeddable
public class UserId {
    @Column(name = "user_id", nullable = false, length = 20, unique = true)
    private String userId;

    public UserId(String userId) {
        validateUserId(userId);
        this.userId = userId;
    }


    private static void validateUserId(String userId) {
        if(userId.isEmpty()) {
            throw new IllegalArgumentException(NOT_VALID_EMPTY + "[아이디]");
        }
    }

    public UserId() {
    }

    public static UserId of(String userId) {
        return new UserId(userId);
    }

    public String getUserId() {
        return this.userId;
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
