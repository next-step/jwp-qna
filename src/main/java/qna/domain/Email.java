package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

    public static final int MAX_EMAIL_LENGTH = 50;

    @Column(length = MAX_EMAIL_LENGTH)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("이메일은 " + MAX_EMAIL_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + email + ")");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
