package qna.domain;

import qna.ForbiddenException;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private final Answers answers = new Answers();

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
        answers.add(answer);
        answer.toQuestion(this);
    }

    public void delete() {
        this.deleted = true;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void deleteByWriter(User writer) {
        this.validateWriter(writer);
        this.validateAnswer(writer);
        this.delete();
    }

    private void validateWriter(User writer) {
        if (this.writer.isNotEquals(writer)) {
            throw new ForbiddenException();
        }
    }

    private void validateAnswer(User writer) {
        if (isNotSameOwner(writer)) {
            validateAnswerIsEmpty();
        }
    }

    private boolean isNotSameOwner(User writer) {
        return this.answers.values().stream()
                .anyMatch(answer -> !answer.isOwner(writer));
    }

    private void validateAnswerIsEmpty() {
        if (this.answers.isNotEmpty()) {
            throw new IllegalStateException();
        }
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

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Answer> getAnswers() {
        return answers.values();
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
