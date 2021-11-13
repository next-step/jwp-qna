package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {
    @Column(nullable = false, length = 20)
    private String userId;
    
    protected UserId() {
    }

    private UserId(String userId) {
        this.userId = userId;
    }

    public static UserId of(String userId) {
        return new UserId(userId);
    }
    
    public boolean isEmpty() {
        return userId.isEmpty();
    }

}
