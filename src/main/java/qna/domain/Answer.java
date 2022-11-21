package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;
    @Embedded
    private Contents contents;
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(User writer, Question question, Contents contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, Contents contents) {
        this.id = id;
        validateAnswer(writer, question);
        this.writer = writer;
        toQuestion(question);
        this.contents = contents;
    }

    private void validateAnswer(User writer, Question question) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    public boolean isOwner(User writer) {
        return Objects.equals(this.writer, writer);
    }

    public void toQuestion(Question question) {
        if (!isDeleted()) {
            question.addAnswer(this);
        }
        this.question = question;
    }

    public DeleteHistory delete(User user) throws CannotDeleteException {
        validateOwner(user);
        validateDeletable();
        setDeleted(true);
        return createDeleteHistory();
    }

    private DeleteHistory createDeleteHistory() {
        return DeleteHistory.of(ContentType.ANSWER, id, writer);
    }


    public void validateOwner(User user) throws CannotDeleteException {
        if (!this.isOwner(user)) {
            throw new CannotDeleteException("답변을 삭제할 권한이 없습니다.");
        }
    }

    private void validateDeletable() throws CannotDeleteException {
        if (isDeleted()) {
            throw new CannotDeleteException("이미 삭제된 답변입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void setDeleted(boolean deleted) {
        if (deleted) {
            question.removeAnswer(this);
        }
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", question=" + question +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
