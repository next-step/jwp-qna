package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Title {
    private static final int MAXIMUM_TITLE_LENGTH = 100;
    public static final String INVALID_TITLE_MESSAGE = "잘못된 타이틀 형식입니다.";

    @Column(length = MAXIMUM_TITLE_LENGTH)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        validate(title);
        this.title = title;
    }

    private void validate(String title) {
        if (Objects.isNull(title) || title.isEmpty() || title.length() > MAXIMUM_TITLE_LENGTH) {
            throw new IllegalArgumentException(INVALID_TITLE_MESSAGE);
        }
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title1 = (Title) o;
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
