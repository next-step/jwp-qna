package qna.domain.wrap;

import java.util.Objects;

import static java.lang.String.format;

public class Name {
    private static final int MAXIMUM_LENGTH = 20;

    private final String name;

    public Name(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 null일 수 없습니다.");
        } else if (name.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("이름은 %d자를 넘길 수 없습니다.", MAXIMUM_LENGTH));
        }
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
