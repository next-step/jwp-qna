package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "question")
@Entity
public class Question extends BaseEntity{
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(name = "writer_id")
    private Long writerId;

    protected Question() {
        //JPA need no-arg constructor
    }

    public Question(String contents, boolean deleted, String title, Long writerId) {
        this.contents = contents;
        this.deleted = deleted;
        this.title = title;
        this.writerId = writerId;
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
