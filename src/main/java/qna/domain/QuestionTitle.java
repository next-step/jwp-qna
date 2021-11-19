package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class QuestionTitle {
    public static final String MAX_TITLE_EXCEPTION_MESSAGE = "title 최대입력 길이를 초과하였습니다.";
    public static final int MAX_TITLE_LENGTH = 100;

    @Column(nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    protected QuestionTitle() {
    }

    protected QuestionTitle(String title) {
        validateTitleLength(title);
        this.title = title;
    }

    private void validateTitleLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(MAX_TITLE_EXCEPTION_MESSAGE);
        }
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionTitle that = (QuestionTitle) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
