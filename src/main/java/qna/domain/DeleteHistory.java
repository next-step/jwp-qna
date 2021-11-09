package qna.domain;

import javax.persistence.*;
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

    @Column(name = "delete_by_id")
    private Long deletedById;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    private DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById) {
        this(null, contentType, contentId, deletedById);
    }

    public DeleteHistory(Long id, ContentType contentType, Long contentId, Long deletedById){
        this.id = id;
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
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
                ", createdDate=" + createdDate +
                '}';
    }
}
