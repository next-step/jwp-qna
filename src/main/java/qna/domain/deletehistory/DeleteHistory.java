package qna.domain.deletehistory;

import org.springframework.data.annotation.CreatedDate;
import qna.domain.ContentType;
import qna.domain.answer.Answer;
import qna.domain.content.ContentId;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Embedded
    private ContentId contentId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    @CreatedDate
    private LocalDateTime createDate;

    public DeleteHistory(ContentType contentType, ContentId contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    protected DeleteHistory() {
    }

    //단순히 생성을 위한 메서드라면 매개변수를 넘기는 것으로 대체
    public static DeleteHistory ofQuestion(ContentId questionId, User writer) {
        return new DeleteHistory(ContentType.QUESTION, questionId, writer, LocalDateTime.now());
    }

    public static DeleteHistory ofAnswer(ContentId answerId, User writer) {
        return new DeleteHistory(ContentType.ANSWER, answerId, writer, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedBy);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deletedBy +
                ", createDate=" + createDate +
                '}';
    }
}
