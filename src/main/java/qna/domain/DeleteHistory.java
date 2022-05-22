package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id")
    private User deleter;

    @CreatedDate
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleter, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleter = deleter;
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
            contentId, that.contentId) && Objects.equals(deleter, that.deleter) && Objects.equals(
            createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleter, createDate);
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
