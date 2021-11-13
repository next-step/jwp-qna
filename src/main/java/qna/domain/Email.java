package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {
    @Column(length = 50)
    private String email;
    
    protected Email() {
    }

    private Email(String email) {
        this.email = email;
    }

    public static Email of(String email) {
        return new Email(email);
    }
    
    public boolean isEmpty() {
        return email.isEmpty();
    }

}
