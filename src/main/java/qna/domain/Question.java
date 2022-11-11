package qna.domain;

import javax.persistence.*;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Lob
    private String contents;
    @ManyToOne()
    @JoinColumn(name = "writer_id")
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {

    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public User getWriter() {
        return this.writer;
    }
}
