package qna.domain;

import static qna.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Username {
    private static final int USERNAME_MAX_LENGTH = 20;

    @Column(length = USERNAME_MAX_LENGTH, nullable = false)
    private String name;

    protected Username() {
    }

    public Username(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        validateBlank(name);
        validateLength(name);
    }

    private void validateBlank(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(VALIDATE_USERNAME_MESSAGE.getMessage());
        }
    }

    private void validateLength(String name) {
        if (name.length() > USERNAME_MAX_LENGTH) {
            throw new IllegalArgumentException(VALIDATE_USERNAME_MESSAGE.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Username username = (Username)o;
        return Objects.equals(name, username.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Username{" +
            "name='" + name + '\'' +
            '}';
    }
}
