package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static qna.domain.ExceptionMessage.ANSWER_DUPLICATED;
import static qna.domain.ExceptionMessage.CANNOT_DELETE_NOT_OWNER;
import static qna.domain.ExceptionMessage.CANNOT_DELETE_OTHERS_ANSWER;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(User writer, String title, String contents) {
        this(null, writer, title, contents);
    }

    public Question(Long id, User writer, String title, String contents) {
        validateAuthorization(writer);

        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void addAnswer(Answer answer) {
        validate(answer);

        answers.add(answer);
        answer.setQuestion(this);
    }

    public DeleteHistories deletedBy(User writer) throws CannotDeleteException {
        validateOwner(writer);

        this.deleted = true;

        return new DeleteHistories(id, writer, answers);
    }

    private void validateOwner(User writer) throws CannotDeleteException {
        validateQuestionOwner(writer);
        validateAnswersOwner(writer);
    }

    private void validateQuestionOwner(User writer) throws CannotDeleteException {
        if (!this.isOwner(writer)) {
            throw new CannotDeleteException(CANNOT_DELETE_NOT_OWNER.getMessage());
        }
    }

    private void validateAnswersOwner(User writer) throws CannotDeleteException {
        if (!answers.stream()
                .allMatch(answer -> answer.isOwner(writer))) {
            throw new CannotDeleteException(CANNOT_DELETE_OTHERS_ANSWER.getMessage());
        }
    }

    private void validateAuthorization(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
    }

    private void validate(Answer answer) {
        checkNull(answer);
        checkDuplicated(answer);
    }

    private void checkNull(Answer answer) {
        if (Objects.isNull(answer)) {
            throw new NotFoundException();
        }
    }

    private void checkDuplicated(Answer answer) {
        if (answers.contains(answer)) {
            throw new IllegalArgumentException(ANSWER_DUPLICATED.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted &&
                Objects.equals(id, question.id) &&
                Objects.equals(title, question.title) &&
                Objects.equals(contents, question.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, deleted);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", answers=" + answers +
                ", deleted=" + deleted +
                '}';
    }
}
