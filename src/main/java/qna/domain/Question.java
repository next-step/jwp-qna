package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
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
        this.answers.deleteAll();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public DeleteHistories deleteByWriter(User writer) throws CannotDeleteException {
        this.validateWriter(writer);
        this.validateAnswer(writer);
        this.delete();
        return getDeleteHistories();
    }

    private DeleteHistories getDeleteHistories() {
        DeleteHistories deleteHistories = new DeleteHistories();
        addQuestionDeleteHistory(deleteHistories);
        addAnswerDeleteHistory(deleteHistories);
        return deleteHistories;
    }

    private void addAnswerDeleteHistory(DeleteHistories deleteHistories) {
        this.answers.values().forEach(answer ->
                deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()))
        );
    }

    private void addQuestionDeleteHistory(DeleteHistories deleteHistories) {
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.id, this.writer, LocalDateTime.now()));
    }

    private void validateWriter(User writer) throws CannotDeleteException {
        if (this.writer.isNotEquals(writer)) {
            throw new CannotDeleteException(ErrorMessage.FORBIDDEN);
        }
    }

    private void validateAnswer(User writer) throws CannotDeleteException {
        if (isNotSameOwner(writer)) {
            validateAnswerIsEmpty();
        }
    }

    private boolean isNotSameOwner(User writer) {
        return this.answers.values().stream()
                .anyMatch(answer -> !answer.isOwner(writer));
    }

    private void validateAnswerIsEmpty() throws CannotDeleteException {
        if (this.answers.isNotEmpty()) {
            throw new CannotDeleteException(ErrorMessage.CANNOT_DELETE);
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
