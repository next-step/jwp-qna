package qna.domain;

import org.springframework.lang.NonNull;
import qna.CannotDeleteException;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity{
    private static final String NOT_DELETE_AUTH_EXCEPTION = "질문을 삭제할 권한이 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NonNull
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    private boolean deleted = false;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {}

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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
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

    public boolean isDeleted() {
        return deleted;
    }

    public List<DeleteHistory> delete(User loginUser, LocalDateTime deletedTime) throws CannotDeleteException {
        validOwner(loginUser);
        answers.validOwner(loginUser);

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addDeleteHistory(new DeleteHistory(ContentType.QUESTION, id, writer, deletedTime));
        deleteHistories.addDeleteHistories(answers.delete(deletedTime));

        this.deleted = true;

        return deleteHistories.getDeleteHistories();
    }

    public Answers getAnswers() {
        return answers;
    }

    public void addAnswers(Answer answer) {
        answers.add(answer);
        answer.toQuestion(this);
    }

    public void validOwner(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(NOT_DELETE_AUTH_EXCEPTION);
        }
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
