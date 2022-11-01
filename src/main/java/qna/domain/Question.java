package qna.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = true)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

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

    public boolean isOwner(User other) {
        return this.writer.equals(other);
    }

    public Long getId() {
        return id;
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setWriter(User writer) {
        if (Objects.nonNull(writer)) {
            this.writer.getQuestions().remove(this);
        }
        this.writer = writer;
        writer.getQuestions().add(this);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.toQuestion(this);
    }

    public List<Answer> getAnswers() {
        return answers;
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
            ", writer=" + writer +
            ", deleted=" + deleted +
            '}';
    }
}
