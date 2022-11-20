package qna.domain;

import qna.message.DeleteHistoryMessage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleter;

    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {

    }

    private DeleteHistory(ContentType contentType, Long contentId, User deleter) {
        validateContentId(contentId);
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleter = deleter;
    }

    private void validateContentId(Long contentId) {
        if (Objects.isNull(contentId)) {
            throw new IllegalArgumentException(DeleteHistoryMessage.ERROR_CONTENT_ID_SHOULD_BE_NOT_NULL.message());
        }
    }

    public static DeleteHistory ofQuestion(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.writer());
    }

    public static DeleteHistory ofAnswer(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.writer());
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeleteHistory that = (DeleteHistory) o;

        if (!Objects.equals(id, that.id)) return false;
        if (contentType != that.contentType) return false;
        return Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (contentId != null ? contentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deleter=" + deleter +
                ", createDate=" + createDate +
                '}';
    }
}
