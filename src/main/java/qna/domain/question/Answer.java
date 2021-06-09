package qna.domain.question;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.UpdatableEntity;
import qna.domain.User;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;

@Entity
@Table
public class Answer extends UpdatableEntity {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Column(columnDefinition = "clob")
    private String contents;

    private boolean deleted = false;

    protected Answer() {}

    public Answer(User writer, Question question, String contents) {

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
        question.addAnswer(this);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public User writer() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
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

    public void deleteBy(User loginUser) throws AnswerOwnerNotMatchedException {
        if (!isOwner(loginUser)) {
            throw new AnswerOwnerNotMatchedException();
        }
        delete();
    }

    private void delete() {
        this.deleted = true;
    }
}
