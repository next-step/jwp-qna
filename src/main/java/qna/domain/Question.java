package qna.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5316964078122252034L;

    public static final String MESSAGE_ALREADY_DELETED = "이미 삭제된 질문입니다.";
    public static final String MESSAGE_HAS_NOT_DELETE_PERMISSION = "질문을 삭제할 권한이 없습니다.";
    public static final String MESSAGE_HAS_OTHER_USER_ANSWER = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private QuestionAnswers questionAnswers;

    @Column
    private boolean deleted = false;

    protected Question() { }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.questionAnswers = new QuestionAnswers();
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        questionAnswers.add(answer);
        answer.replyTo(this);
    }

    public DeleteHistory deleteAnswer(Answer answer, LocalDateTime deleteTime) {
        return questionAnswers.delete(answer, deleteTime);
    }

    public DeleteHistories deleteAnswers(LocalDateTime deleteTime) {
        return questionAnswers.deleteAnswers(deleteTime);
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

    public List<Answer> getAnswers() {
        return new ArrayList<>(questionAnswers.getAnswers());
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistories delete(User loginUser, LocalDateTime deleteTime)
        throws CannotDeleteException {

        verifyDeletedQuestion();
        verifyDeletePermission(loginUser);
        verifyHasOtherUserAnswer();

        return delete(deleteTime);
    }

    private DeleteHistories delete(LocalDateTime deleteTime) {

        this.deleted = true;

        DeleteHistories deleteHistories = new DeleteHistories(DeleteHistory.ofQuestion(id, writer, deleteTime));
        return deleteHistories.addAll(deleteAnswers(deleteTime));
    }

    private void verifyDeletedQuestion() throws CannotDeleteException {
        if (isDeleted()) {
            throw new CannotDeleteException(MESSAGE_ALREADY_DELETED);
        }
    }

    private void verifyDeletePermission(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(MESSAGE_HAS_NOT_DELETE_PERMISSION);
        }
    }

    private void verifyHasOtherUserAnswer() throws CannotDeleteException {
        if (questionAnswers.hasOtherUserAnswer(writer)) {
            throw new CannotDeleteException(MESSAGE_HAS_OTHER_USER_ANSWER);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
