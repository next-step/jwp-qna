package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import qna.domain.wrappers.ContentId;

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
    @Enumerated(value = EnumType.STRING)
    private ContentType contentType;

    @Embedded
    private ContentId contentId = new ContentId();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    private DeleteHistory(ContentType contentType, long contentId, User deletedBy) {
        this.contentType = contentType;
        this.contentId = new ContentId(contentId);
        this.deletedBy = deletedBy;
    }

    public static DeleteHistory create(ContentType contentType, Long contendId, User user) {
        return new DeleteHistory(contentType, contendId, user);
    }

    public Long getId() {
        return id;
    }

    public void changeContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public boolean isSameContentType(ContentType contentType) {
        return this.contentType == contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedBy, that.deletedBy) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedBy, createDate);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", " + contentId.toString() +
                ", deletedById=" + deletedBy.id() +
                ", createDate=" + createDate +
                '}';
    }
}
