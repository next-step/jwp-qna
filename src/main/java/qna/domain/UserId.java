package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
        if (isEmpty(userId)) {
            throw new IllegalArgumentException("아이디를 입력하지 않았습니다.");
        }
        if (userId.length() > USER_ID_MAX_SIZE) {
            throw new IllegalArgumentException(String.format("아이디의 길이가 %d자를 초과했습니다.", USER_ID_MAX_SIZE));
        }
    }
    
    private static boolean isEmpty(String userId) {
        return userId.isEmpty();
    }

}
