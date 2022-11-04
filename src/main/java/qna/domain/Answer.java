package qna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.Objects;

@Entity
public class Answer extends DateEntity {

    @Column(nullable = true)
    private Long writerId;

    @Column(nullable = true)
    private Long questionId;

    @Lob
    @Column(nullable = true)
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() { }

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

        this.writerId = writer.getId();
        this.questionId = question.getId();
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.questionId = question.getId();
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writerId +
                ", questionId=" + questionId +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
