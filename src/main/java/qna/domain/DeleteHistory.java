package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id")
    private User writer;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User writer) {
        this.contentId = contentId;
        deleteBy(writer);
    }

    private void deleteBy(User writer) {
        if (this.writer != null) {
            this.writer.getDeleteHistories().remove(this);
        }
        this.writer = writer;
        writer.getDeleteHistories().add(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DeleteHistory that = (DeleteHistory)obj;
        return contentId.equals(that.contentId) && contentType == that.contentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId, contentType);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentType=" + contentType +
            ", contentId=" + contentId +
            ", deletedById=" + writer.getId() +
            ", createDate=" + createDate +
            '}';
    }
}
