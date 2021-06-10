package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.lang.String.format;

@Embeddable
public class Name {
    public static final int MAXIMUM_LENGTH = 20;

    @Column(length = MAXIMUM_LENGTH, nullable = false)
    private String name;

    protected Name() {

    }

    public Name(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 null일 수 없습니다.");
        }
        if (name.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("이름의 최대 길이는 %d자 입니다.", MAXIMUM_LENGTH));
        }
    }

    @Override
    public String toString() {
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
