package qna.domain.question;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.domain.UpdatableEntity;
import qna.domain.User;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.exception.question.QuestionNotDeletedException;
import qna.domain.exception.question.QuestionOwnerNotMatchedException;
import qna.domain.history.DeleteHistories;

@Entity
@Table
public class Question extends UpdatableEntity {

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "clob")
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private final Answers answers = new Answers();

    private boolean deleted = false;

    protected Question() {}

    public Question(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public User writer() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistories deleteBy(User loginUser) throws
            QuestionOwnerNotMatchedException,
            AnswerOwnerNotMatchedException {
        if (!this.isOwner(loginUser)) {
            throw new QuestionOwnerNotMatchedException();
        }
        this.deleted = true;
        DeleteHistories deletedAnswers = answers.deleteAllBy(loginUser);
        DeleteHistories deletedQuestion;
        try {
            deletedQuestion = new DeleteHistories(this);
        } catch (QuestionNotDeletedException e) {
            // 발생하지 않으므로 RuntimeException 적용.
            throw new RuntimeException(e);
        }
        return new DeleteHistories(deletedQuestion, deletedAnswers);
    }

    public Answer addAnswer(Answer answer) {
        this.answers.add(answer);
        return answer;
    }

    public Answer addAnswer(User answerUser, String content) {
        Answer answer = new Answer(answerUser, this, content);
        addAnswer(answer);
        return answer;
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

}
