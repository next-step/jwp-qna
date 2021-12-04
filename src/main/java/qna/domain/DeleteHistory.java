package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long contentId;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public static DeleteHistory ofQuestion(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());
    }

    public static DeleteHistory ofAnswer(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now());
    }

    public DeleteHistory(ContentType contentType, Long contentId, User user, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.user = user;
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
                ", userId=" + user.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
