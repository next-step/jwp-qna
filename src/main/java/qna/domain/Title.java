package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.constant.ErrorCode;

@Embeddable
public class Title {

    private static final int MAX_LENGTH = 50;

    @Column(nullable = false, length = 100)
    private String title;

    protected Title() {

    }

    private Title(String title) {
        validateLength(title);
        this.title = title;
    }

    public static Title of(String title) {
        return new Title(title);
    }

    private void validateLength(String title) {
        if(title.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(ErrorCode.제목의_길이가_너무_김.getErrorMessage());
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
