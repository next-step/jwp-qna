package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.constant.ErrorCode;

@Embeddable
public class Name {

    private static final int MAX_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String name;

    protected Name() {
    }

    private Name(String name) {
        validateLength(name);
        this.name = name;
    }

    public static Name of(String name) {
        return new Name(name);
    }

    private void validateLength(String name) {
        if(name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(ErrorCode.이름의_길이가_너무_김.getErrorMessage());
        }
    }

    public boolean isEqualName(Name name) {
        return this.equals(name);
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

    @Override
    public String toString() {
        return "Name{" +
                "name='" + name + '\'' +
                '}';
    }
}
