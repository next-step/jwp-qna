package qna.question;

import qna.CannotDeleteException;
import qna.answer.Answer;
import qna.domain.BaseEntity;
import qna.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseEntity{
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
            throw new IllegalArgumentException("질문은 필수로 입력 해야 합니다.");
        }
        return question;
    }

    public void throwExceptionNotDeletableUser(final User loginUser) throws CannotDeleteException {
        if (!this.user.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void throwExceptionNotDeletableAnswersInQuestion(final User loginUser) throws CannotDeleteException {
        answers.throwExceptionNotDeletableAnswers(loginUser);
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

    public void delete(User loginUser) throws CannotDeleteException {
        throwExceptionNotDeletableUser(loginUser);
        deleteAnswers(loginUser);
        deleted = true;
    }

    public void deleteAnswers(User loginUser) throws CannotDeleteException {
        throwExceptionNotDeletableAnswersInQuestion(loginUser);
        answers.delete();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted
                && Objects.equals(getId(), question.getId())
                && Objects.equals(user, question.user)
                && Objects.equals(contents, question.contents)
                && Objects.equals(title, question.title)
                && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), user, contents, deleted, title, answers);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", user=" + user +
                ", deleted=" + deleted +
                '}';
    }
}
