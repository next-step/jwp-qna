package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleteUser;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    private DeleteHistory(ContentType contentType, Long contentId, User deleteUser) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteUser = deleteUser;
    }

    public static DeleteHistory createByAnswer(Long contentId, User deleteUser) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deleteUser);
    }

    public static DeleteHistory createByQuestion(Long contentId, User deleteUser) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deleteUser);
    }

    protected DeleteHistory() {

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
                Objects.equals(deleteUser, that.deleteUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleteUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deleteUser=" + deleteUser +
                ", createDate=" + createDate +
                '}';
    }
}
