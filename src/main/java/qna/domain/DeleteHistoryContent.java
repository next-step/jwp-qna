package qna.domain;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class DeleteHistoryContent {

    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private Long contentId;

    protected DeleteHistoryContent() {
    }

    protected DeleteHistoryContent(ContentType contentType, Long contentId) {
        this.contentType = contentType;
        this.contentId = contentId;
    }

    public static DeleteHistoryContent ofAnswer(Answer answer) {
        return new DeleteHistoryContent(ContentType.ANSWER, answer.getId());
    }

    public static DeleteHistoryContent ofQuestion(Question question) {
        return new DeleteHistoryContent(ContentType.QUESTION, question.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistoryContent that = (DeleteHistoryContent) o;
        return contentType == that.contentType &&
               Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, contentId);
    }

    @Override
    public String toString() {
        return "DeleteHistoryContent{" +
               "contentType=" + contentType +
               ", contentId=" + contentId +
               '}';
    }
}
