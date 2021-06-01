package qna.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type", length = 255)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name="fk_delete_history_to_user"))
    private User user;

    protected DeleteHistory() {

    }

    public DeleteHistory(ContentType contentType, Answer answer, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = answer.getId();
        this.user = answer.getWriter();
        this.createDate = createDate;
    }

    public DeleteHistory(ContentType contentType, Question question, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = question.getId();
        this.user = question.getWriter();
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
                Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, user.getId());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + user.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
