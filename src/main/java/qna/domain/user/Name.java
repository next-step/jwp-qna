package qna.domain.user;

import org.springframework.context.support.MessageSourceAccessor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static qna.constant.Message.NOT_VALID_EMPTY;

@Embeddable
public class Name {
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    public Name(String name) {
        this.name = name;
    }

    public Name() {
    }

    public static Name of(String name) {
        validateName(name);
        return new Name(name);
    }

    private static void validateName(String name) {
        if(name.isEmpty()) {
            throw new IllegalArgumentException(NOT_VALID_EMPTY + "[이름]");
        }
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
