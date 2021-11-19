package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.exception.IllegalNameException;
import qna.domain.exception.NameLengthExceedException;

@Embeddable
public class Name {

    private static final int LENGTH_LIMIT = 20;

    @Column(nullable = false, length = LENGTH_LIMIT)
    private String name;

    protected Name() {
    }

    private Name(final String name) {
        validateNotNullOrEmpty(name);
        validateLengthNotExceed(name);

        this.name = name;
    }

    private void validateNotNullOrEmpty(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalNameException();
        }
    }

    private void validateLengthNotExceed(String name) {
        if (name.length() > LENGTH_LIMIT) {
            throw new NameLengthExceedException(LENGTH_LIMIT);
        }
    }

    public static Name from(final String name) {
        return new Name(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
