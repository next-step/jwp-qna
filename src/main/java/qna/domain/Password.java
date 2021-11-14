package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.ValidationUtils;

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
        ValidationUtils.checkEmpty(password);
        ValidationUtils.checkLength(password, PASSWORD_MAX_SIZE);
    }
    
}
