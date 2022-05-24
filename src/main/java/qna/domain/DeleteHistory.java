package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column
    private ContentType contentType;
    @Column
    private Long contentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "deletedById",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_delete_history_to_user"),
            nullable = false
    )
    private User deleted;
    @Column
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleted, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleted = deleted;
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
                Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleted);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deleted=" + deleted +
                ", createDate=" + createDate +
                '}';
    }
}
