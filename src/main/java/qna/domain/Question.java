package qna.domain;

import static qna.constants.ExceptionMessage.INVALID_DELETE_QUESTION_BECAUSE_ANSWER_WRITER_NON_MATCH;
import static qna.constants.ExceptionMessage.INVALID_DELETE_QUESTION_BECAUSE_NON_MATCH_WRITER_USER;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import qna.CannotDeleteException;

@Entity
@Table(name = "question")
public class Question extends BaseDateTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Lob
    @Column(name = "contents")
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    protected Question() {}

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long id() {
        return id;
    }

    public User writer() {
        return this.writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void toDeleted(User loginUser, List<Answer> answers) throws CannotDeleteException {
        validateDeleteAuthority(loginUser);
        validateDeleteAnswersAuthority(loginUser, answers);
        this.changeDeleted(true);
    }

    private void validateDeleteAnswersAuthority(User owner, List<Answer> answers) throws CannotDeleteException {
        for (Answer answer : answers) {
            validateDeleteAnswerAuthority(owner, answer);
        }
    }

    private void validateDeleteAnswerAuthority(User owner, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(owner)) {
            throw new CannotDeleteException(String.format(INVALID_DELETE_QUESTION_BECAUSE_ANSWER_WRITER_NON_MATCH, owner.userId()));
        }
    }

    private void validateDeleteAuthority(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(String.format(INVALID_DELETE_QUESTION_BECAUSE_NON_MATCH_WRITER_USER, loginUser.userId()));
        }
    }

    private void changeDeleted(boolean deleted) {
        this.deleted = deleted;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return Objects.equals(id(), question.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
