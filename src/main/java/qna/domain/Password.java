package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.StringValidateUtils;

@Embeddable
public class Password {

    public static final int LENGTH = 20;

    @Column(name = "password", nullable = false, length = LENGTH)
    private String password;

    protected Password() {
    }

    public Password(final String password) {
        this.password = StringValidateUtils.validate(password, LENGTH);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Password password1 = (Password)o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
