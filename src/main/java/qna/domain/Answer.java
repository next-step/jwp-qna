package qna.domain;

import static qna.constants.ExceptionMessage.INVALID_DELETE_QUESTION_BECAUSE_ANSWER_WRITER_NON_MATCH;

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

@Entity
@Table(name = "answer")
public class Answer extends BaseDateTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;
    @Lob
    @Column(name = "contents")
    private String contents;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    protected Answer() {}

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long id() {
        return id;
    }

    public User writer() {
        return this.writer;
    }

    public Question question() {
        return this.question;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory toDeleted(User owner) throws CannotDeleteException {
        validateDeleteAnswerAuthority(owner);
        this.changeDeleted(true);

        return DeleteHistory.ofAnswer(this.id, owner);
    }

    private void validateDeleteAnswerAuthority(User owner) throws CannotDeleteException {
        if (!this.isOwner(owner)) {
            throw new CannotDeleteException(String.format(INVALID_DELETE_QUESTION_BECAUSE_ANSWER_WRITER_NON_MATCH, owner.userId()));
        }
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    private void changeDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(id(), answer.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
