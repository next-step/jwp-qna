package qna.domain;

import static javax.persistence.FetchType.LAZY;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.message.ErrorMessage;


@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    public Answer() {}

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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
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

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return deleted == answer.deleted && Objects.equals(writer, answer.writer) && Objects.equals(question, answer.question) && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(writer, question, contents, deleted);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer +
                ", questionId=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    public DeleteHistory delete(User loginUser) throws CannotDeleteException {
        checkWriter(loginUser);
        setDeleted(true);
        return new DeleteHistory(ContentType.ANSWER, getId(), getWriter(), LocalDateTime.now());
    }

    private void checkWriter(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(ErrorMessage.EXISTS_ANOTHER_USER_ANSWER);
        }
    }
}
