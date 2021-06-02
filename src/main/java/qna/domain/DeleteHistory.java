package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

@Table
@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;

    @Column
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(Question question, User writer) {
        this(ContentType.QUESTION, question.getId(), writer);
    }

    public DeleteHistory(Answer answer, User writer) {
        this(ContentType.ANSWER, answer.getId(), writer);
    }

    private DeleteHistory(ContentType contentType, Long contentId, User deletedByUser) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        DeleteHistory that = (DeleteHistory)object;

        return Objects.equals(id, that.id)
            && contentType == that.contentType
            && Objects.equals(contentId, that.contentId)
            && Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedByUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{"
            + "id=" + id
            + ", contentType=" + contentType
            + ", contentId=" + contentId
            + ", deletedById=" + deletedByUser.getId()
            + ", createDate=" + createDate
            + '}';
    }
}
