package qna.domain;

import qna.domain.converter.ContentTypeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    /**
     * create table delete_history
     * (
     * id            bigint generated by default as identity,
     * content_id    bigint,
     * content_type  varchar(255),
     * create_date   timestamp,
     * deleted_by_id bigint,
     * primary key (id)
     * )
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long contentId;
    // @Convert(converter = ContentTypeConverter.class)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private LocalDateTime createDate = LocalDateTime.now();
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deleter_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleter;

    public DeleteHistory(ContentType contentType, Long contentId, User deleter, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleter = deleter;
        this.createDate = createDate;
    }

    public DeleteHistory() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deleter.getId(), that.deleter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleter.getId());
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
