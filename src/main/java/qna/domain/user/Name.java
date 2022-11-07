package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {

    @Column(length = 20, nullable = false)
    private String name;

    public Name(String name) {
        this.name = name;
    }

    public Name() {

    }

    public static Name of(String name) {
        return new Name(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name email1 = (Name) o;
        return Objects.equals(name, email1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
