package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Content {
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User writer;

    protected Content(ContentType contentType, Long contentId, User writer) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.writer = writer;
    }

    public static Content ofAnswer(Answer answer) {
        return new Content(ContentType.ANSWER, answer.getId(), answer.getWriter());
    }

    public static Content ofQuestion(Question question) {
        return new Content(ContentType.QUESTION, question.getId(), question.getWriter());
    }

    protected Content() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Content)) {
            return false;
        }
        Content content = (Content)o;
        return contentType == content.contentType &&
            Objects.equals(contentId, content.contentId) &&
            Objects.equals(writer, content.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, contentId, writer);
    }
}
