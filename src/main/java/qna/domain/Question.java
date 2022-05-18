package qna.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Lob
    @Column(name = "contents", columnDefinition = "CLOB")
    private String contents;
    @Column(name = "writer_id")
    private Long writerId;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
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

    public Long getId() {
        return this.id;
    }

    public Long getWriterId() {
        return this.writerId;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(this.id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + this.id +
                ", title='" + this.title + '\'' +
                ", contents='" + this.contents + '\'' +
                ", writerId=" + this.writerId +
                ", deleted=" + this.deleted +
                '}';
    }
}
