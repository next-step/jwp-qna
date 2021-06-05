package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.StringValidateUtils;

@Embeddable
public class UserId {

    public static final int LENGTH = 20;

    @Column(name = "user_id", nullable = false, length = LENGTH, unique = true)
    private String value;

    protected UserId() {
    }

    public UserId(final String value) {
        this.value = StringValidateUtils.validate(value, LENGTH);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserId userId = (UserId)o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
