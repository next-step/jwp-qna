package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

    @Column(length = 50)
    private String email;

    public Email(String email) {
        if (email.length() > 50) {
            throw new IllegalArgumentException("Email이 올바르지 않습니다.");
        }
        this.email = email;
    }

    public Email() {

    }

    public String getEmail() {
        return email;
    }
}
