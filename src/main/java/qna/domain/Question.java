package qna.domain;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User user;
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
        this.user = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.user.equals(writer);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getQuestions().remove(this);
        }
        
        this.user = user;
        user.addQuestion(this);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", user=" + user +
            ", deleted=" + deleted +
            '}';
    }
}
