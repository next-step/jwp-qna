package qna.domain.content;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static qna.constant.Message.NOT_VALID_EMPTY;

@Embeddable
public class Title {
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    public Title(String title) {
        validateTitle(title);
        this.title = title;
    }

    public Title() {
    }

    public static Title of(String title) {
        return new Title(title);
    }

    private void validateTitle(String title) {
        if(title.isEmpty()) {
            throw new IllegalArgumentException(NOT_VALID_EMPTY + "[제목]");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title email1 = (Title) o;
        return Objects.equals(title, email1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
