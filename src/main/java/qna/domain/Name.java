package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Name {
    private static final int MAXIMUM_NAME_LENGTH = 20;
    private static final Pattern NAME_PATTERN = PatternEnum.NAME.toPattern();
    public static final String INVALID_NAME_MESSAGE = "잘못된 이름 형식입니다.";

    @Column(length = MAXIMUM_NAME_LENGTH, nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (Objects.isNull(name) || name.isEmpty() || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new IllegalArgumentException(INVALID_NAME_MESSAGE);
        }
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            throw new IllegalArgumentException(INVALID_NAME_MESSAGE);
        }
    }

    public String getName() {
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
