package qna.domain.qna;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import qna.common.exception.InvalidParamException;

@Embeddable
public class QuestionPost {

    public static final int TITLE_LENGTH = 100;

    @Column(name = "title", length = TITLE_LENGTH, nullable = false)
    private String title;

    @Embedded
    Contents contents;

    protected QuestionPost() {
    }

    private QuestionPost(String title, String contents) {
        valid(title);

        this.title = title;
        this.contents = Contents.of(contents);
    }

    public static QuestionPost of(String title, String contents) {
        return new QuestionPost(title, contents);
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents.getContents();
    }

    private void valid(String title) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new InvalidParamException("제목을 입력해주세요.");
        }

        if (title.length() > TITLE_LENGTH) {
            throw new InvalidParamException(String.format("제목은 %s자 까지 입력가능합니다.", TITLE_LENGTH));
        }
    }

    @Override
    public String toString() {
        return "QuestionPost{" +
            "title='" + title + '\'' +
            ", contents=" + contents +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuestionPost that = (QuestionPost) o;
        return Objects.equals(title, that.title)
            && Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, contents);
    }
}
