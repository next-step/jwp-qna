package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    private static final int MAX_LENGTH = 20;
    private static final String INVALID_MESSAGE = "길이 " + MAX_LENGTH + "이하의 비어있지 않은 이름을 입력해주세요.";

    @Column(length = MAX_LENGTH, nullable = false)
    private String name;

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(INVALID_MESSAGE);
        }
    }

    protected Name() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Name)) {
            return false;
        }
        Name name1 = (Name)o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
