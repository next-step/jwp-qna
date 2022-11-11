package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {

    @Column(name = "user_id", length = 20, nullable = false, unique = true)
    private String userId;

    public UserId() {
    }

    public UserId(String userId) {
        this.userId = userId;
    }
}
