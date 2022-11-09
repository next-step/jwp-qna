package qna.domain.deleteHistory;

import qna.domain.answer.Answer;
import qna.domain.answer.Answers;
import qna.domain.ContentType;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "deleteHistory")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "content_type", length = 255)
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @JoinColumn(name = "deleted_by_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    @ManyToOne
    private User deleteByUser;


    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleteByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteByUser = deleteByUser;
        this.createDate = createDate;
    }

    public static DeleteHistory ofQuestionDeletedHistory(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());
    }

    public static DeleteHistory ofAnswerDeletedHistory(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now());
    }

    public static List<DeleteHistory> addDeleteQuestionHistory(Question question) {
        List<DeleteHistory> deleteHistoryList = new ArrayList<>();
        deleteHistoryList.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));

        Answers answers = new Answers(question.getAnswers());
        for(Answer answer : answers) {
            deleteHistoryList.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistoryList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deleteByUser, that.deleteByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleteByUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deleteByUser.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
