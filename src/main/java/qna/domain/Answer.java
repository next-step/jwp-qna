package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.common.ErrorMessage;

@Entity
@Table
public class Answer extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        validWriteAnswer(writer, question);

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    private static void validWriteAnswer(User writer, Question question) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    public DeleteHistory delete(User loginUser) {
        validWriter(loginUser);
        this.deleted = true;
        return DeleteHistory.answerOf(this.id, loginUser);
    }

    private void validWriter(User loginUser) {
        if (writer.isNotWriter(loginUser)) {
            throw new CannotDeleteException(ErrorMessage.WRITTEN_BY_SOMEONE_ELSE_ANSWER);
        }
    }

    public void addQuestion(Question question) {
        this.question = question;
    }

    public void changeQuestion(Question question) {
        if (Objects.nonNull(this.question)) {
            question.removeAnswer(this);
        }
        this.question = question;
        question.addAnswer(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isNotDeleted() {
        return !deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return deleted == answer.deleted
                && Objects.equals(id, answer.id)
                && Objects.equals(writer, answer.writer)
                && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
