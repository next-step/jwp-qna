package qna.domain.wrappers;

import qna.util.ValidationUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Title {
    private static final int TITLE_MAX_LENGTH = 100;

    @Column(nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        ValidationUtil.checkValidNullOrEmpty(title);
        ValidationUtil.checkValidTitleLength(title, TITLE_MAX_LENGTH);
        this.title = title;
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
        return "title= " + title;
    }
}
