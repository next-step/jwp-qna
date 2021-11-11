package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById) {
        this(null, contentType, contentId, deletedById);
    }

    public DeleteHistory(Long id, ContentType contentType, Long contentId, Long deletedById) {
        this.id = id;
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
    }

    public Long getId() {
        return id;
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
