package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User user;

    public DeleteHistory() { }

    public DeleteHistory(ContentType contentType, Long contentId, User user, LocalDateTime createDate) {
        this(null, contentType, contentId, user, createDate);
    }

    public DeleteHistory(Long id, ContentType contentType, Long contentId, User user, LocalDateTime createDate) {
        this.id = id;
        this.contentType = contentType;
        this.contentId = contentId;
        this.user = user;
    }

    protected Long getId() {
        return id;
    }

    protected User getUser() {
        return user;
    }

    protected Long getContentId() {
        return contentId;
    }

    protected void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    protected ContentType getContentType() {
        return this.contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, user);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", user=" + user +
                '}';
    }
}
