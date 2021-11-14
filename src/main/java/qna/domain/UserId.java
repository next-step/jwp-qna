package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.ValidationUtils;

@Embeddable
public class UserId {
    public static final int USER_ID_MAX_SIZE = 20;
    
    @Column(nullable = false, length = USER_ID_MAX_SIZE)
    private String userId;
    
    protected UserId() {
    }

    private UserId(String userId) {
        this.userId = userId;
    }

    public static UserId of(String userId) {
        checkValidation(userId);
        return new UserId(userId);
    }
    
    private static void checkValidation(String userId) {
        ValidationUtils.checkEmpty(userId);
        ValidationUtils.checkLength(userId, USER_ID_MAX_SIZE);
    }
    
}
