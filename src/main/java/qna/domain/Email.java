package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

    @Column(name = "email", length = 50)
    private String email;

    public Email() {

    }

    public Email(String email) {
        this.email = email;
    }
}
