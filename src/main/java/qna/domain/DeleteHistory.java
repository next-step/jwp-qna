package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "delete_history")
@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User user;

    private DeleteHistory(ContentType contentType, Long contentId, User user, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.user = user;
        this.createDate = createDate;
    }

    public static DeleteHistory forAnswerOf(Long answerId, User user) {
        return new DeleteHistory(ContentType.ANSWER, answerId, user, LocalDateTime.now());
    }

    public static DeleteHistory forQuestionOf(Long questionId, User user) {
        return new DeleteHistory(ContentType.QUESTION, questionId, user, LocalDateTime.now());
    }

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

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
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
                ", deletedById=" + user.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
