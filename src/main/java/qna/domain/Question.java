package qna.domain;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question extends DateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User user;

    @Lob
    @Column(name = "contests")
    private String contents;

    @Column(name = "delete", nullable = false)
    private boolean deleted = false;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    protected Question() {
    }

    public Question writeBy(final User user) {
        this.user = user;
        return this;
    }

    public boolean isOwner(final User user) {
        return this.user.equals(user);
    }

    public void addAnswer(final Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final boolean deleted) {
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
