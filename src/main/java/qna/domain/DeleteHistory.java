package qna.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ContentType contentType;

    @Column(nullable = true)
    private Long deletedById;

    @Column(nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
        this.createDate = createDate;
    }

    public static DeleteHistories of(Question question) {
        return DeleteHistories.of(ofQuestion(question), ofAnswers(question.getAnswers()));
    }

    private static DeleteHistory ofQuestion(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriterId(), LocalDateTime.now());
    }

    private static List<DeleteHistory> ofAnswers(List<Answer> answers) {
        return answers.stream().map(DeleteHistory::ofAnswer).collect(Collectors.toList());
    }

    private static DeleteHistory ofAnswer(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriterId(), LocalDateTime.now());
    }

    public Long getContentId() {
        return contentId;
    }

    public boolean isQuestion() {
        return this.contentType == ContentType.QUESTION;
    }

    public boolean isAnswer() {
        return this.contentType == ContentType.ANSWER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeleteHistory that = (DeleteHistory)o;
        return Objects.equals(id, that.id) &&
            contentType == that.contentType &&
            Objects.equals(contentId, that.contentId) &&
            Objects.equals(deletedById, that.deletedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedById);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentType=" + contentType +
            ", contentId=" + contentId +
            ", deletedById=" + deletedById +
            ", createDate=" + createDate +
            '}';
    }
}
