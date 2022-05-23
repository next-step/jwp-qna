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
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer extends AuditTimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this(id, contents, question, writer, false);
    }

    private Answer(Long id, String contents, Question question, User writer, boolean deleted) {
        validateWriter(writer);
        validateQuestion(question);

        this.id = id;
        this.contents = contents;
        this.question = Question.from(question);
        this.writer = User.from(writer);
        this.deleted = deleted;
    }

    protected Answer() {
    }

    public static Answer from(Answer answer) {
        if (Objects.isNull(answer)) {
            return null;
        }
        return new Answer(answer.id, answer.contents, answer.question, answer.writer, answer.deleted);
    }

    public DeleteHistory delete(User user) throws CannotDeleteException {
        if (!isOwner(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        setDeleted(true);
        return DeleteHistory.of(ContentType.ANSWER, id, writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Long getQuestionId() {
        return question.getId();
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    private void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private void validateQuestion(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    private void validateWriter(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", question_id=" + question.getId() +
                ", writer_id=" + writer.getId() +
                ", getCreatedAt()=" + getCreatedAt() +
                ", getUpdatedAt()=" + getUpdatedAt() +
                '}';
    }
}
