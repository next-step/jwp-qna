package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    private Long writerId;

    @Column(nullable = false)
    private boolean deleted = false;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Question(final String title, final String contents) {
        this(null, title, contents);
    }

    public Question(final Long id, final String title, final String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(final User writer) {
        this.writerId = writer.getId();
        return this;
    }

    public boolean isOwner(final User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void addAnswer(final Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(final Long writerId) {
        this.writerId = writerId;
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
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", writerId=" + writerId +
            ", deleted=" + deleted +
            '}';
    }
}
