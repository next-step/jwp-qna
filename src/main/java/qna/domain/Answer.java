package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.Objects;

import javax.persistence.CascadeType;
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

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {
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
    private boolean deleted = Boolean.FALSE;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        if (Objects.isNull(writer) || writer.isGuestUser()) {
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
        this.question.removeAnswer(this);
        this.question = question;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory delete(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        this.deleted = true;

        return DeleteHistory.OfAnswer(this);
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", writerId=" + writer.getId() +
            ", questionId=" + question.getId() +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answer answer = (Answer)o;
        return deleted == answer.deleted
            && Objects.equals(id, answer.id)
            && Objects.equals(writer, answer.writer)
            && Objects.equals(question, answer.question)
            && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }
}
