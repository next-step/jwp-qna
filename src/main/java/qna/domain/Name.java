package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.StringValidateUtils;

@Embeddable
public class Name {

    public static final int LENGTH = 20;

    @Column(name = "name", nullable = false, length = LENGTH)
    private String value;

    protected Name() {
    }

    public Name(final String name) {
        this.value = StringValidateUtils.validate(name, LENGTH);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Name name = (Name)o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
