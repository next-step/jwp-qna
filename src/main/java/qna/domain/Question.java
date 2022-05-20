package qna.domain;

import static qna.constants.ExceptionMessage.INVALID_DELETE_QUESTION_BECAUSE_NON_MATCH_WRITER_USER;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
    @Embedded
    private final Answers answers = Answers.createEmpty();

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

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.add(answer);
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

    public DeleteHistories toDeleted(User loginUser) throws CannotDeleteException {
        validateDeleteAuthority(loginUser);
        this.deleted(true);

        return generateDeleteHistories(loginUser);
    }

    private DeleteHistories generateDeleteHistories(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = DeleteHistories.empty();
        deleteHistories.add(DeleteHistory.ofQuestion(id, loginUser));
        deleteHistories.merge(this.answers.deleteAll(loginUser));
        return deleteHistories;
    }

    private void validateDeleteAuthority(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(String.format(INVALID_DELETE_QUESTION_BECAUSE_NON_MATCH_WRITER_USER, loginUser.userId()));
        }
    }

    private void deleted(boolean deleted) {
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
