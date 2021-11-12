package qna.domain.question;

import qna.domain.BaseTimeEntity;
import qna.domain.answer.Answer;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Deleted deleted = new Deleted();

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    private Question(String title, String contents) {
        this.title = new Title(title);
        this.contents = new Contents(contents);
    }

    public static Question create(String title, String contents) {
        return new Question(title, contents);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void changeDeleteState(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        deleted.setTrue();
        answers.changeAnswerState(loginUser);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public Long getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public List<Answer> getAnswers() {
        return answers.getAnswers();
    }
}
