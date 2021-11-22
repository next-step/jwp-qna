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

@EntityListeners(AuditingEntityListener.class)
@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @CreatedDate
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleter;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeleteHistory that = (DeleteHistory)o;
        return Objects.equals(id, that.id) && Objects.equals(contentId, that.contentId)
            && contentType == that.contentType && Objects.equals(createDate, that.createDate)
            && Objects.equals(deleter, that.deleter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentId, contentType, createDate, deleter);
    }
}
