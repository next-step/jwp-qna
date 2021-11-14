package qna.question;

import qna.CannotDeleteException;
import qna.action.NullCheckAction;
import qna.answer.Answer;
import qna.domain.DateTimeEntity;
import qna.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends DateTimeEntity implements NullCheckAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
        this(null, title, contents, user);
    }

    public Question(final Long id, final String title, final String contents, final User user) {
        throwExceptionIsNullObject(user);
        this.id = id;
        this.title = new Title(title);
        this.contents = contents;
        this.user = user;
    }

    protected Question() {
    }


    public void throwExceptionNotDeletableUser(final User loginUser) throws CannotDeleteException {
        if (!this.user.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void throwExceptionNotDeletableAnswersInQuestion(final User loginUser) throws CannotDeleteException {
        answers.throwExceptionNotDeletableAnswers(loginUser);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void addAnswer(Answer answer) {
        this.answers.addAnswer(answer);
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers.getAnswers());
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void deleteQuestion() {
        this.deleted = true;
    }

    public void deleteAnswers() {
        answers.changeDeletedAnswer();
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
