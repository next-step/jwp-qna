package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
@Table
public class Answer extends UpdatableEntity {

    @ManyToOne
    private User writer;

    @ManyToOne
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

    public void delete() {
        this.deleted = true;
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
}
