package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {
    @Column(nullable = false, length = 20)
    private String password;
    
    protected Password() {
    }

    private Password(String password) {
        this.password = password;
    }

    public static Password of(String password) {
        return new Password(password);
    }
    
    public boolean isEmpty() {
        return password.isEmpty();
    }

}
