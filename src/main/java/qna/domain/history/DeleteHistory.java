package qna.domain.history;

import org.springframework.data.annotation.CreatedDate;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(updatable = false)
    private Long contentId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    private DeleteHistory(ContentType contentType, Long contentId, User deletedByUser) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
    }

    public static DeleteHistory createBy(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter());
    }

    public static DeleteHistory createBy(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedByUser);
    }
}
