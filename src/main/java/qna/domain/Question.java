package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers;

    @Column(nullable = false)
    private boolean deleted = false;

    public Question() { }

    public Question(String title, String contents) {
        this(title, null, contents);
    }

    public Question(Long id, String title, String contents) {
        this(id, title, null, contents);
    }

    public Question(String title, User writer, String contents) {
        this(null, title, writer, contents);
    }

    public Question(Long id, String title, User writer, String contents) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.contents = contents;
        this.answers = new Answers();
    }

    public DeleteHistories deleteAndHistories(User loginUser) throws CannotDeleteException {
        validateUserForDelete(loginUser);

        DeleteHistories deleteHistories = this.deleteAndHistories();
        deleteHistories.add(answers.deleteAllAndHistories());

        return deleteHistories;
    }

    protected DeleteHistories deleteAndHistories() {
        this.setDeleted(true);

        return new DeleteHistories(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
    }

    private void validateUserForDelete(User loginUser) throws CannotDeleteException {
         validateQuestionProprietary(loginUser);
         validateAnswersProprietary(loginUser);
    }

    protected void validateQuestionProprietary(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    protected void validateAnswersProprietary(User loginUser) throws CannotDeleteException {
        answers.validateProprietary(loginUser);
    }

    public Question writtenBy(User writer) {
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

    public void setWriter(User user) {
        this.writer = user;
    }

    public List<Answer> getAnswers() {
        return answers.answers();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted
                && Objects.equals(id, question.id)
                && Objects.equals(title, question.title)
                && Objects.equals(contents, question.contents)
                && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted);
    }

}
