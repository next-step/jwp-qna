package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseContentEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    public Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        super(id, writer, contents);

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.question = question;
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public DeleteHistory delete(User loginUser) throws CannotDeleteException {
        validateOwnerAndSetDeleted(loginUser);
        return new DeleteHistory(ContentType.ANSWER, getId(), loginUser, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Answer{" +
                ", question=" + question +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(getId(), answer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
