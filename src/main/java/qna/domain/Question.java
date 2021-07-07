package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Lob
    private String contents;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name="fk_question_writer"), name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<Answer>();

    public Question() {
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
        return this.writer.isEqual(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }
}
