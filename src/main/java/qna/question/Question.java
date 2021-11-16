package qna.question;

import qna.exception.CannotDeleteException;
import qna.answer.Answer;
import qna.domain.BaseEntity;
import qna.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseEntity{
    private static final String CAN_NOT_DELETE = "질문을 삭제할 권한이 없습니다.";
    private static final String QUESTION_IS_REQUIRED = "질문은 필수로 입력 해야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User user;

    @Lob
    @Column(name = "contests")
    private String contents;

    @Column(name = "delete", nullable = false)
    private boolean deleted = false;

    @Embedded
    private Title title;

    @Embedded
    private final Answers answers = new Answers();

    public Question(final String title, final String contents, final User user) {
        this.title = new Title(title);
        this.contents = contents;
        this.user = User.getOrElseThrow(user);
    }

    protected Question() {
    }

    public static Question getOrElseThrow(Question question){
        if(Objects.isNull(question)){
            throw new IllegalArgumentException(QUESTION_IS_REQUIRED);
        }
        return question;
    }

    public void throwExceptionNotDeletableUser(final User loginUser) {
        if (!this.user.equals(loginUser)) {
            throw new CannotDeleteException(CAN_NOT_DELETE);
        }
    }

    public void throwExceptionNotDeletableAnswersInQuestion(final User loginUser) {
        answers.throwExceptionNotDeletableAnswers(loginUser);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public Answers getAnswers() {
        return answers;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(User loginUser) {
        throwExceptionNotDeletableUser(loginUser);
        deleteAnswers(loginUser);
        deleted = true;
    }

    public void deleteAnswers(User loginUser) {
        throwExceptionNotDeletableAnswersInQuestion(loginUser);
        answers.delete();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted
                && Objects.equals(id, question.id)
                && Objects.equals(user, question.user)
                && Objects.equals(contents, question.contents)
                && Objects.equals(title, question.title)
                && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, contents, deleted, title, answers);
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
