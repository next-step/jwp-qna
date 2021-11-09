package qna.domain.deletehistory;

import qna.domain.ContentType;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Embedded
    private ContentId contentId;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    private LocalDateTime createDate = LocalDateTime.now();

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = new ContentId(contentId);
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public DeleteHistory() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedBy);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deletedBy.getId() +
                ", createDate=" + createDate +
                '}';
    }

    public static DeleteHistory ofAnswer(Long contentId, User deletedBy, LocalDateTime createDate) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedBy, createDate);
    }

    public static DeleteHistory ofQuestion(Long contentId, User deletedBy, LocalDateTime createDate) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedBy, createDate);
    }

}
