package qna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User deletedByUser;

    protected DeleteHistory() {

    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) && contentType == that.contentType && Objects.equals(contentId, that.contentId) && Objects.equals(deletedByUser,
                that.deletedByUser);
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
                ", createDate=" + createDate +
                ", deletedByUser=" + deletedByUser +
                '}';
    }
}
