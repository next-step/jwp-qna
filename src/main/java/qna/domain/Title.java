package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.message.ExceptionMessage;

@Embeddable
public class Title {

    private static final int MAX_TITLE_LENGTH = 100;
    @Column(nullable = false, length = 100)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        this.title = title;
    }

    public static Title from(String title) {
        validateNotNull(title);
        validateLength(title);
        return new Title(title);
    }

    private static void validateNotNull(String title) {
        if (title == null) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_TITLE);
        }
    }

    private static void validateLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(String.format(ExceptionMessage.INVALID_TITLE_LENGTH, title.length()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Title title1 = (Title) o;
        return title.equals(title1.title);
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
