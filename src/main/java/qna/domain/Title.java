package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {
    private static final int MAX_LENGTH = 100;
    private static final String INVALID_MESSAGE = "길이 100이하의 비어있지 않은 제목을 입력해주세요.";

    @Column(length = MAX_LENGTH, nullable = false)
    private String title;

    public Title(String title) {
        validateTitle(title);
        this.title = title;
    }

    private void validateTitle(String title) {
        if (title == null || title.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(INVALID_MESSAGE);
        }
    }

    protected Title() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Title)) {
            return false;
        }
        Title title1 = (Title)o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
