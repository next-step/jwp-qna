package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
        if (isEmpty(email)) {
            throw new IllegalArgumentException("이메일을 입력하지 않았습니다.");
        }
        if (email.length() > EMAIL_MAX_SIZE) {
            throw new IllegalArgumentException(String.format("이메일 길이가 %d자를 초과했습니다.", EMAIL_MAX_SIZE));
        }
    }
    
    private static boolean isEmpty(String email) {
        return email.isEmpty();
    }

}
