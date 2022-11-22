package qna.domain;

import qna.ForbiddenException;
import qna.constant.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Title {
    private static final int TITLE_LENGTH = 100;

    public static Title of(String title) {
        return new Title(title);
    }

    @Column(nullable = false, length = TITLE_LENGTH)
    private String title;

    protected Title() {

    }

    private Title(String title) {
        validateTitle(title);
        this.title = title;
    }

    private void validateTitle(String title) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.제목_공백.getErrorMessage());
        }
        if (title.length() > TITLE_LENGTH) {
            throw new IllegalArgumentException(ErrorCode.제목_길이_초과.getErrorMessage());
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
