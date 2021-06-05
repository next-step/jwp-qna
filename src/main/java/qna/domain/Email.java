package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.StringValidateUtils;

@Embeddable
public class Email {

    public static final int LENGTH = 50;

    @Column(name = "email", length = LENGTH)
    private String value;

    protected Email() {
    }

    public Email(final String email) {
        value = StringValidateUtils.validate(email, LENGTH);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Email email = (Email)o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
