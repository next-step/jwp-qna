package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {
    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    protected Answer() {
    }

    private Answer(Long id, User writer, Question question, String contents) {
        super(id);
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public static Answer of(User writer, Question question, String contents) {
        return of(null, writer, question, contents);
    }

    public static Answer of(Long id, User writer, Question question, String contents) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        Answer answer = new Answer(id, writer, question, contents);
        question.addAnswer(answer);
        return answer;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public Question getQuestion() {
        return question;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean hasSameQuestion(Question question) {
        return this.question == question;
    }

    public void delete(User principal) throws CannotDeleteException {
        if (!isOwner(principal)) {
            throw new CannotDeleteException("답변 작성자만 삭제할 수 있습니다");
        }
        this.deleted = true;
        question.removeAnswer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Answer answer = (Answer) o;
        return deleted == answer.deleted && Objects.equals(contents, answer.contents) && Objects.equals(question, answer.question) && Objects.equals(writer, answer.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contents, deleted, question, writer);
    }
}
