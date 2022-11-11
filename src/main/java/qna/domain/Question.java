package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseContentEntity {
    @Column(nullable = false, length = 100)
    private String title;
    @Embedded
    private Answers answers;

    public Question() {
        this.answers = new Answers();
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super(id, contents);
        this.title = title;
        this.answers = new Answers();
    }

    public Question writeBy(User writer) {
        super.writeBy(writer);
        return this;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public List<Answer> answers() {
        return answers.answers();
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validateOwnerAndSetDeleted(loginUser);

        DeleteHistories deleteHistories = new DeleteHistories(new DeleteHistory(ContentType.QUESTION, getId(), loginUser, LocalDateTime.now()));
        deleteHistories.addAll(answers.delete(loginUser));

        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Question{" +
                ", title='" + title + '\'' +
                '}';
    }
}
