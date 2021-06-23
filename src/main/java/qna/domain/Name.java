package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {

    public static final int NAME_MAXIMUM_LENGTH = 20;
    public static final String INVALID_NAME_VALUE = "Invalid Name value";

    @Column(nullable = false, length = NAME_MAXIMUM_LENGTH)
    private String name;

    public Name(String name) {
        this.name = validName(name);
    }

    public Name() {
    }

    private String validName(String name) {
        if (Objects.isNull(name) || name.isEmpty() || name.length()>NAME_MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(INVALID_NAME_VALUE);
        }
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
