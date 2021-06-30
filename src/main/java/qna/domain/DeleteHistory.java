package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "delete_history")
@Entity
public class DeleteHistory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "delete_by_id",
            foreignKey = @ForeignKey(name = "fk_delete_history_to_user")
    )
    private User user;

    public DeleteHistory(ContentType contentType, Question question, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = question.getId();
        this.user = question.getWriter();
        this.createDate = createDate;
    }

    public DeleteHistory(ContentType contentType, Answer answer, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = answer.getId();
        this.user = answer.getWriter();
        this.createDate = createDate;
    }

    public DeleteHistory() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, user);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", userId=" + user.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
