package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class DeleteHistoryContent {

    @Column
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column
    private Long contentId;

    protected DeleteHistoryContent() {
    }

    private DeleteHistoryContent(ContentType contentType, Long removeContentId) {
        this.contentType = contentType;
        this.contentId = removeContentId;
    }

    public static DeleteHistoryContent remove(Question question) {
        return new DeleteHistoryContent(ContentType.QUESTION, question.getId());
    }

    public static DeleteHistoryContent remove(Answer answer) {
        return new DeleteHistoryContent(ContentType.ANSWER, answer.getId());
    }

    @Override
    public String toString() {
        return "DeleteHistoryContent{" +
                "contentType=" + contentType +
                ", contentId=" + contentId +
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
        DeleteHistoryContent that = (DeleteHistoryContent) o;
        return contentType == that.contentType && Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, contentId);
    }
}
