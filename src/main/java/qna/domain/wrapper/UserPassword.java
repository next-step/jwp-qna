package qna.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.lang.String.format;
import static qna.domain.option.LengthLimitation.MAXIMUM_USER_PASSWORD_LENGTH;
import static qna.domain.option.LengthLimitation.MINIMUM_USER_PASSWORD_LENGTH;

@Embeddable
public class UserPassword {

    @Column(length = MAXIMUM_USER_PASSWORD_LENGTH, nullable = false)
    private String password;

    public UserPassword() { }

    public UserPassword(String password) {
        if (password == null || password.length() < MINIMUM_USER_PASSWORD_LENGTH || password.length() > MAXIMUM_USER_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(format("유저 비밀번호는 필수로, 길이는 %d~ %d 입니다.",
                    MINIMUM_USER_PASSWORD_LENGTH, MAXIMUM_USER_PASSWORD_LENGTH));
        }
        this.password = password;
    }

    public String get() {
        return this.password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPassword that = (UserPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
