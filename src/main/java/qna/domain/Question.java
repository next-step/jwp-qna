package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents", columnDefinition = "LONGTEXT")
    private String contents;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "writer_id")
    private Long writerId;


    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        validateTitle(title);
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
    }

    private void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title을 입력하세요");
        }
    }

    public Question writeBy(User writer) {
        this.writerId = writer.getId();
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Long getWriterId() {
        return writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                ", deleted=" + deleted +
                ", title='" + title + '\'' +
                ", updatedAt=" + updatedAt +
                ", writerId=" + writerId +
                '}';
    }
}
