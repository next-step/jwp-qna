package qna.domain;

import static qna.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import qna.exception.CannotDeleteException;
import qna.exception.UnAuthorizedException;

@Entity
public class Question extends BaseEntityTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @Embedded
    private Answers answers = new Answers();

    @OneToOne
    @JoinColumn(name = "write_id")
    private User writer;

    private boolean deleted = false;

    protected Question() {
    }

    public Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    public Question(Long id, String title, String contents, User writer) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
    }

    public void addAnswer(Answer answer) {
        answer.changeQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public Answers getAnswers() {
        return answers;
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

    public void validateQuestionOwner(User owner) {
        if (!isOwner(owner)) {
            throw new CannotDeleteException(CANNOT_DELETE_QUESTION_MESSAGE.getMessage());
        }
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void delete(User owner) {
        validateQuestionDelete(owner);
        deleted = true;
        answers.deleteAll();
    }

    private void validateQuestionDelete(User owner) {
        validateQuestionOwner(owner);
        getAnswers().validateAnswersOwner(owner);
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
