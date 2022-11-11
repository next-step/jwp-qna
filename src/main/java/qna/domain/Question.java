package qna.domain;

import org.hibernate.annotations.Where;
import qna.NotFoundException;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class  Question extends TimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;
    @Column(nullable = false, columnDefinition = "bit")
    private boolean deleted = false;
    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = false")
    private List<Answer> answers = new ArrayList<>();

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

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (deleted) {
            throw new NotFoundException();
        }
        answer.toQuestion(this);
        this.answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (Objects.isNull(writer)) {
            return null;
        }
        return writer.getId();
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
                ", writer=" + writer.toString() +
                ", deleted=" + deleted +
                '}';
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public void deleteAnswer(Answer deletedAnswer) {
        getAnswers().stream()
            .filter(deletedAnswer::equals)
            .forEach(answer -> answer.setDeleted(true));
        this.answers = getAnswers().stream()
            .filter(answer -> !answer.isDeleted())
            .collect(Collectors.toList());
    }
}
