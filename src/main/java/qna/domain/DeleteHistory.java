package qna.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    private DeleteHistory(ContentType contentType, Long contentId, User deletedBy) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public static DeleteHistory ofQuestion(Long contentId, User deletedBy) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedBy);
    }

    public static DeleteHistory ofAnswer(Long contentId, User deletedBy) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedBy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteHistory)) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedBy=" + deletedBy +
                ", createDate=" + createDate +
                '}';
    }
}
