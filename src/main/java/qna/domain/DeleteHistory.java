package qna.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private ContentType contentType;

    @Column
    private Long contentId;

    @Column
    private Long deletedById;

    @CreatedDate
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedById, that.deletedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedById);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deletedById +
                ", createDate=" + createDate +
                '}';
    }
}
