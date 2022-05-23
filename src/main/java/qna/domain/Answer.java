package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {
    private static final String NOT_ANSWER_WRITER = "답변의 작성자가 아니므로 삭제할 수 없습니다.";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;
    @Lob
    @Column(name = "contents", columnDefinition = "CLOB")
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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public void delete(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(NOT_ANSWER_WRITER);
        }
        this.deleted(Boolean.TRUE);
    }

    private void deleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return this.id;
    }

    public User getWriter() {
        return this.writer;
    }

    public Question getQuestion() {
        return this.question;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    // TODO delete 구현 완료 후 삭제
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(this.id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + this.id +
                ", writer=" + this.writer +
                ", question=" + this.question +
                ", contents='" + this.contents + '\'' +
                ", deleted=" + this.deleted +
                '}';
    }
}
