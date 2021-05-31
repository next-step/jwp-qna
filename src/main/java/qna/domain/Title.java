package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Title {
    private static final String INVALID_INPUT = "올바르지 않은 입력입니다.";

    @Column(nullable = false)
    private String title;

    public Title() {
    }

    public Title(String title) {
        this.title = validate(title);
    }

    private String validate(String title) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        return title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Title)) return false;
        Title title1 = (Title) o;
        return Objects.equals(getTitle(), title1.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }

    @Override
    public String toString() {
        return "Title{" +
                "title='" + title + '\'' +
                '}';
    }
}
