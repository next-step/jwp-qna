package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.ValidationUtils;

@Embeddable
public class Email {
    public static final int EMAIL_MAX_SIZE = 50;
    
    @Column(length = EMAIL_MAX_SIZE)
    private String email;
    
    protected Email() {
    }

    private Email(String email) {
        this.email = email;
    }

    public static Email of(String email) {
        checkValidation(email);
        return new Email(email);
    }
    
    private static void checkValidation(String email) {
        ValidationUtils.checkEmpty(email);
        ValidationUtils.checkLength(email, EMAIL_MAX_SIZE);
    }

}
