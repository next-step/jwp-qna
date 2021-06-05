package qna.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name", nullable = false, length = 20)
    private String value;

    protected Name() {
    }

    public Name(final String name) {
        this.value = validName(name);
    }

    private String validName(final String name) {
        return Optional.ofNullable(name)
            .filter(string -> string.trim().length() > 0)
            .filter(string -> string.trim().length() <= 20)
            .map(String::trim)
            .orElseThrow(IllegalArgumentException::new);
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
