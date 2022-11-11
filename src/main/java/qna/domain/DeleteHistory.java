package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Enumerated(STRING)
    @Column
    private ContentType contentType;
    private Long contentId;
    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User whoDeleted;
    @CreatedDate
    private LocalDateTime createDate;

    public DeleteHistory(ContentType contentType, Long contentId, User whoDeleted, LocalDateTime createDate) {
        this(null, contentType, contentId, whoDeleted, createDate);
    }

    public DeleteHistory(Long id, ContentType contentType, Long contentId, User whoDeleted, LocalDateTime createDate) {
        this.id = id;
        this.contentType = contentType;
        this.contentId = contentId;
        this.whoDeleted = whoDeleted;
        this.createDate = createDate;
    }

    protected DeleteHistory() {

    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(whoDeleted, that.whoDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, whoDeleted);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", whoDeleted=" + whoDeleted.toString() +
                ", createDate=" + createDate +
                '}';
    }
}
