package qna.domain;

import qna.exception.CannotDeleteException;
import qna.exception.type.QuestionExceptionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private final Answers answers = new Answers();

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void deleted() {
        this.deleted = true;
    }
    public List<DeleteHistory> delete(User loginUser) {
        validCheckDeleteQuestion(loginUser);
        deleted();

        return deleteHistories(this, loginUser);
    }

    private List<DeleteHistory> deleteHistories(Question question, User loginUser) {
        return makeCombineHistories(
                answers.delete(loginUser),
                DeleteHistory.of(ContentType.QUESTION, question)
        );
    }

    private List<DeleteHistory> makeCombineHistories(List<DeleteHistory> deleteHistories, DeleteHistory deleteHistory) {
        List<DeleteHistory> combineHistory = new ArrayList<>();
        combineHistory.add(deleteHistory);
        combineHistory.addAll(deleteHistories);
        return combineHistory;
    }

    private void validCheckDeleteQuestion(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(QuestionExceptionType.NOT_OWNER_LOGIN_USER.getMessage());
        }

        if (isDeleted()) {
            throw new CannotDeleteException(QuestionExceptionType.ALREADY_DELETE_QUESTION.getMessage());
        }
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
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(title, question.title) && Objects.equals(contents, question.contents) && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted);
    }
}
