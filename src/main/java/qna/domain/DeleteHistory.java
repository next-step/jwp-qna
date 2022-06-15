package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "deleted_by_id",
        foreignKey = @ForeignKey(name = "FK_DELETE_HISTORY_TO_USER"),
        nullable = false
    )
    private User deletedBy;

    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public boolean isQuestion() {
        return contentType.isQuestion();
    }

    public boolean isAnswer() {
        return contentType.isAnswer();
    }

    public static List<DeleteHistory> createQuestionDeleteHistory(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        if (question.getAnswers().size() > 0) {
            deleteHistories.addAll(createAnswerDeleteHistory(question.getAnswers()));
        }
        DeleteHistory questionDeleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(),
            LocalDateTime.now());
        deleteHistories.add(questionDeleteHistory);
        return deleteHistories;
    }

    private static List<DeleteHistory> createAnswerDeleteHistory(List<Answer> answers) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentType=" + contentType +
            ", contentId=" + contentId +
            ", deletedBy=" + deletedBy +
            ", createDate=" + createDate +
            '}';
    }
}
