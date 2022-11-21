package qna.domain;

import qna.CannotDeleteException;
import qna.constant.DeleteErrorMessage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question {
    private static final boolean DELETED_FLAG = true;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers = new Answers();

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
        answer.addQuestion(this);
        this.answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validateWriterSameLoginUser(loginUser);
        validateAnswerWriterSameQuestionWriter(loginUser);

        setDeleted(DELETED_FLAG);

        DeleteHistories deleteHistories = answers.delete(loginUser);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, loginUser, LocalDateTime.now()));
        return deleteHistories;

    }

    private void validateWriterSameLoginUser(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(DeleteErrorMessage.NOT_HAVE_PERMISSION_DELETE_QUESTION);
        }
    }

    private void validateAnswerWriterSameQuestionWriter(User loginUser) throws CannotDeleteException {
        if (!answers.isEmpty() && !answers.validateWriterSameLoginUser(loginUser)) {
            throw new CannotDeleteException(DeleteErrorMessage.NOT_EMPTY_ANSWER_DELETE_QUESTION);
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(title, question.title) && Objects.equals(contents, question.contents) && Objects.equals(writer, question.writer) && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted, answers);
    }
}
