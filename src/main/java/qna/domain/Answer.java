package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long writerId;

    private Long questionId;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
