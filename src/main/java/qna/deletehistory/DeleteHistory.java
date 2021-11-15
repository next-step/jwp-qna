package qna.deletehistory;

import qna.answer.Answer;
import qna.domain.ContentType;
import qna.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delete_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;

    @Column(name = "created_date")
    private final LocalDateTime createdDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(final ContentType contentType, final Long contentId, final User deletedByUser) {
        this(null, contentType, contentId, deletedByUser);
    }

    public DeleteHistory(final Long id, final ContentType contentType, final Long contentId, final User deletedByUser) {
        this.id = id;
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = User.getOrElseThrow(deletedByUser);;
    }

    public static DeleteHistory of(Answer answer){
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getUser());
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.getId())
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
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedByUser=" + deletedByUser +
                ", createdDate=" + createdDate +
                '}';
    }
}
