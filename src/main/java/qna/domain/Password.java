package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {
    private static final String INVALID_INPUT = "올바르지 않은 입력입니다.";
    private static final int MAXIMUM_PASSWORD_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String password;

    public Password() {
    }

    public Password(String password) {
        this.password = validate(password);
    }

    private String validate(String password) {
        if (Objects.isNull(password) || password.isEmpty() || password.length() > MAXIMUM_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        return password;
    }

    public String getPassword() {
        return password;
    }

    public void matchPasswordWith(User target) {
        if (!this.password.equals(target.getPassword())) {
            throw new UnAuthorizedException();
        }
    }
}
