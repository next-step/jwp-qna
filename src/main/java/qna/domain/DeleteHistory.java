package qna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User deleteBy;

    @Column
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleteBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteBy = deleteBy;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
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

        return Objects.equals(id, that.id) &&
            contentType == that.contentType &&
            Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentType=" + contentType +
            ", contentId=" + contentId +
            ", deletedById=" + deleteBy.getId() +
            ", createDate=" + createDate +
            '}';
    }
}
