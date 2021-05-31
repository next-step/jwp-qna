package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User deleteUser;

    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleteUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteUser = deleteUser;
        this.createDate = createDate;
    }

    public static DeleteHistory of(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.writer(), LocalDateTime.now());
    }

    public static DeleteHistory of(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.writer(), LocalDateTime.now());
    }

    public User deleteUser() {
        return deleteUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deleteUser.getId(), that.deleteUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleteUser.getId());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deleteUser.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
