package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {
    public static final int PASSWORD_MAX_SIZE = 20;
    
    @Column(nullable = false, length = PASSWORD_MAX_SIZE)
    private String password;
    
    protected Password() {
    }

    private Password(String password) {
        checkValidation(password);
        this.password = password;
    }

    public static Password of(String password) {
        return new Password(password);
    }
    
    private static void checkValidation(String password) {
        if (isEmpty(password)) {
            throw new IllegalArgumentException("비밀번호를 입력하지 않았습니다.");
        }
        if (password.length() > PASSWORD_MAX_SIZE) {
            throw new IllegalArgumentException(String.format("비밀번호 길이가 %d자를 초과했습니다.", PASSWORD_MAX_SIZE));
        }
    }
    
    private static boolean isEmpty(String password) {
        return password.isEmpty();
    }
}
