package qna.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "answer")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE answer SET deleted = true WHERE id = ?")
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writeBy;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;
    @Embedded
    private Contents contents;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Answer(User writeBy, Question question, Contents contents) {
        this(null, writeBy, question, contents);
    }

    public Answer(Long id, User writeBy, Question question, Contents contents) {
        this.id = id;

        if (Objects.isNull(writeBy)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writeBy = writeBy;
        this.question = question;
        this.contents = contents;
    }

    protected Answer() {
    }

    public boolean isOwner(User writer) {
        return this.writeBy.equals(writer);
    }

    public void updateQuestion(Question question) {
        if (this.question != null) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        question.getAnswers().add(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriteBy() {
        return writeBy;
    }

    public Question getQuestion() {
        return question;
    }

    public Contents getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(User loginUser) throws CannotDeleteException {
        valid(loginUser);
        this.deleted = true;
    }

    private void valid(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", writer=" + writeBy +
            ", questionId=" + question +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }
}
