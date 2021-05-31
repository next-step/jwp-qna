package qna.domain;

import qna.CannotDeleteException;
import qna.domain.wrapper.Answers;
import qna.domain.wrapper.DeleteHistories;
import qna.domain.wrapper.Deleted;
import qna.domain.wrapper.QuestionTitle;

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

    @Embedded
    private QuestionTitle title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers;

    @Embedded
    private Deleted deleted;

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
        this.title = new QuestionTitle(title);
        this.writer = writer;
        this.contents = contents;
        this.answers = new Answers();
        this.deleted = new Deleted();
    }

    public DeleteHistories deleteBy(User loginUser) throws CannotDeleteException {
        validateDeletionBy(loginUser);

        DeleteHistories deleteHistories = this.deleteAndReturnHistories();
        deleteHistories.add(answers.deleteAllAndHistories());

        return deleteHistories;
    }

    public DeleteHistories deleteAndReturnHistories() {
        this.delete();

        return new DeleteHistories(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
    }

    private void delete() {
        deleted.setTrue();
    }

    private void validateDeletionBy(User loginUser) throws CannotDeleteException {
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

    public QuestionTitle getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted.status();
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
