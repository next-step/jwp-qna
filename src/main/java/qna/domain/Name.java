package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.message.ExceptionMessage;

@Embeddable
public class Name {
    private static final int MAX_NAME_LENGTH = 20;
    @Column(nullable = false, length = 20)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        this.name = name;
    }

    public static Name from(String name) {
        validateNotNull(name);
        validateLength(name);
        return new Name(name);
    }

    private static void validateNotNull(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_NAME);
        }
    }

    private static void validateLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format(ExceptionMessage.INVALID_NAME_LENGTH, name.length()));
        }
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
        return name.equals(name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Name{" +
                "name='" + name + '\'' +
                '}';
    }
}
