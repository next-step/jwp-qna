package qna.domain;

import static qna.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {
    private static final int TITLE_MAX_LENGTH = 100;

    @Column(length = TITLE_MAX_LENGTH, nullable = false)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        validate(title);
        this.title = title;
    }

    private void validate(String title) {
        validateBlank(title);
        validateLength(title);
    }

    private void validateBlank(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException(VALIDATE_TITLE_MESSAGE.getMessage());
        }
    }

    private void validateLength(String title) {
        if (title.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException(VALIDATE_TITLE_MESSAGE.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Title title1 = (Title)o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Title{" +
            "title='" + title + '\'' +
            '}';
    }
}
