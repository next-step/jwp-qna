package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @JoinColumn(name = "deleted_by_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User deletedUser;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {

    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedUser = deletedUser;
        this.createDate = createDate;
    }


    public Long getId() {
        return id;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void changeContentType(ContentType contentType) {
        this.contentType = contentType;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public User getDeletedUser() {
        return deletedUser;
    }

    public void setDeletedUser(User deletedUser) {
        this.deletedUser = deletedUser;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
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
        return Objects.equals(id, that.id) && contentType == that.contentType && Objects.equals(
                contentId, that.contentId) && Objects.equals(deletedUser, that.deletedUser);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedUser, createDate);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedUser=" + deletedUser +
                ", createDate=" + createDate +
                '}';
    }
}
