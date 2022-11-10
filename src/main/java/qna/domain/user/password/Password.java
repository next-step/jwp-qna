package qna.domain.user.password;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;

@Embeddable
public class Password {

    private static final int LIMIT_LENGTH = 20;

    @Column(length = LIMIT_LENGTH, nullable = false)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        NullAndEmptyValidator.getInstance().validate(password);
        LengthValidator.getInstance().validate(password, LIMIT_LENGTH);
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Password)) {
            return false;
        }
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    public String getPassword() {
        return password;
    }
}
