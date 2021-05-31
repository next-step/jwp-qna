package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    private static final String INVALID_INPUT = "올바르지 않은 입력입니다.";
    private static final int MAXIMUM_NAME_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        this.name = validate(name);
    }

    private String validate(String name) {
        if (Objects.isNull(name) || name.isEmpty() || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        return name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name)) return false;
        Name name1 = (Name) o;
        return Objects.equals(getName(), name1.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Name{" +
                "name='" + name + '\'' +
                '}';
    }
}
