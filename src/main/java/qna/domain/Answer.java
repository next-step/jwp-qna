package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

import static qna.utils.ValidationUtils.isEmpty;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", updatable = false, foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", updatable = false, foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Answer(User writer, Question question, String contents) {
        validation(writer, question);
        this.writer = writer;
        this.question = question;
        this.contents = contents;
        question.addAnswer(this);
    }

    protected Answer() {
    }

    private void validation(User writer, Question question) {
        if (isEmpty(writer)) {
            throw new UnAuthorizedException("답변 작성자가 존재하지 않습니다.");
        }
        if (isEmpty(question)) {
            throw new NotFoundException("질문이 존재하지 않습니다.");
        }
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        if (isDeleted()) {
            throw new CannotDeleteException("이미 삭제된 답변입니다.");
        }
        changeDeletedTrue();
    }

    private void changeDeletedTrue() {
        this.deleted = true;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
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

    public User getWriter() {
        return writer;
    }
}
