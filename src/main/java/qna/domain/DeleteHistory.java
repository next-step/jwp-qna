package qna.domain;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import qna.UnAuthorizedException;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(value = STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id")
    private User deletedByUser;

    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        if (Objects.isNull(deletedByUser)) {
            throw new UnAuthorizedException();
        }
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser) {
        this(contentType, contentId, deletedByUser, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Long getContentId() {
        return contentId;
    }

    public User getDeletedByUser() {
        return deletedByUser;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeleteHistory that = (DeleteHistory)o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (contentType != that.contentType) {
            return false;
        }
        if (!Objects.equals(contentId, that.contentId)) {
            return false;
        }
        return Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (contentId != null ? contentId.hashCode() : 0);
        result = 31 * result + (deletedByUser != null ? deletedByUser.hashCode() : 0);
        return result;
    }
}
