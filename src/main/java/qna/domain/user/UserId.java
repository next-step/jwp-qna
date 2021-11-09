package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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

    public String getUserId() {
        return userId;
    }
}
