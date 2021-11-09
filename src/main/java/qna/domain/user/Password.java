package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {

    @Column(length = 20, nullable = false)
    private String password;

    public Password(String password) {
        if (password.isEmpty() || password.length() > 20) {
            throw new IllegalArgumentException("Password가 올바르지 않습니다.");
        }
        this.password = password;
    }

    public Password() {

    }

    public String getPassword() {
        return password;
    }
}
