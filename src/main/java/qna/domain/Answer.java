package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "writer_id")
    private Long writerId;

    @Column(name = "question_id")
    private Long questionId;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(final User writer, final Question question, final String contents) {
        this(null, writer, question, contents);
    }

    public Answer(final Long id, final User writer, final Question question, final String contents) {
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

    public boolean isOwner(final User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void toQuestion(final Question question) {
        this.questionId = question.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(final Long writerId) {
        this.writerId = writerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(final Long questionId) {
        this.questionId = questionId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Answer answer = (Answer)o;
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(writerId,
            answer.writerId) && Objects.equals(questionId, answer.questionId) && Objects.equals(
            contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writerId, questionId, contents, deleted);
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
