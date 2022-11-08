package qna.domain.content;

import qna.domain.history.DeleteHistoryGenerator;
import qna.exception.CannotDeleteException;
import qna.exception.UnAuthorizedException;
import qna.common.AuditingEntity;
import qna.domain.history.DeleteHistory;
import qna.domain.User;
import qna.exception.message.QuestionExceptionCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Question extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Embedded
    private Answers answers = new Answers();


    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {}

    public Question(User writer, String title, String contents) {
        this(null, writer, title, contents);
    }

    public Question(Long id, User writer, String title, String contents) {
        validateForInserts(writer, title);

        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    private void validateForInserts(User writer, String title) {
        validateWriter(writer);
        validateTitle(title);
    }

    private void validateWriter(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException(QuestionExceptionCode.REQUIRED_WRITER.getMessage());
        }
    }

    private void validateTitle(String title) {
        if (Objects.isNull(title)) {
            throw new IllegalArgumentException(QuestionExceptionCode.REQUIRED_TITLE.getMessage());
        }
    }

    public List<DeleteHistory> delete(User loginUser) {
        checkDeletableQuestion(loginUser);
        this.deleted = true;

        return createDeleteHistories(loginUser);
    }

    private void checkDeletableQuestion(User loginUser) {
        if(!isOwner(loginUser)) {
            throw new CannotDeleteException(QuestionExceptionCode.NOT_MATCH_LOGIN_USER.getMessage());
        }

        if(isDeleted()) {
            throw new CannotDeleteException(QuestionExceptionCode.ALREADY_DELETED.getMessage());
        }
    }

    private List<DeleteHistory> createDeleteHistories(User loginUser) {
        return DeleteHistoryGenerator.combine(
                    DeleteHistoryGenerator.generate(ContentType.QUESTION, id, getWriter()),
                        answers.deleteAnswers(loginUser));
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    void addAnswer(Answer answer) {
        answers.addAnswer(answer, this);
    }

    public boolean hasAnswer(Answer answer) {
        return answers.hasAnswer(answer);
    }

    public void update(User loginUser, String title, String contents) {
        validateForUpdates(loginUser, title);

        this.title = title;
        this.contents = contents;
    }

    private void validateForUpdates(User loginUser, String title) {
        matchUser(loginUser);
        validateTitle(title);
    }

    private void matchUser(User loginUser) {
        if(!this.writer.equals(loginUser)) {
            throw new UnAuthorizedException(QuestionExceptionCode.NOT_MATCH_LOGIN_USER.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
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
        return Objects.equals(id, question.id) && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer);
    }
}
