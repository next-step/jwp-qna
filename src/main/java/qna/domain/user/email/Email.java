package qna.domain.user.email;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.validate.string.LengthValidator;

@Embeddable
public class Email {

    private static final int LIMIT_LENGTH = 50;

    @Column(length = LIMIT_LENGTH)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        LengthValidator.getInstance().validate(email, LIMIT_LENGTH);
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public String getEmail() {
        return email;
    }
}
