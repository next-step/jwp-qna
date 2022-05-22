package qna.domain;

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
    @Column(name = "content_type")
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleter;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleter, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleter = deleter;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public ContentType getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deleter, that.deleter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleter);
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
