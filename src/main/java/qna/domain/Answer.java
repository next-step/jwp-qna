package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "answer")
public class Answer {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents")
    @Lob
    private String contents;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "question_id")
    private Long questionId;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "writer_id")
    private Long writerId;

    protected Answer() {

    }

    public Answer(String contents, boolean deleted, Long questionId, Long writerId) {
        this.id = null;
        this.contents = contents;
        this.deleted = deleted;
        this.questionId = questionId;
        this.writerId = writerId;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Answer answer = (Answer)obj;
        return id.equals(answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
