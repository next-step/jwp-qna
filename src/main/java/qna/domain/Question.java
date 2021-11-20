package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "question")
@Entity
public class Question extends BaseEntity {

    protected static final String NO_DELETE_PERMISSION = "질문을 삭제할 권한이 없습니다.";
    protected static final String ANSWER_CAN_NOT_BE_NULL = "answer는 null일 수 없습니다.";
    protected static final String DUPLICATE_ANSWER = "이미 등록된 answer입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @OneToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answers = new ArrayList<>();

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    private Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public static Question of(String title, String contents) {
        return new Question(title, contents);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (answer == null) {
            throw new IllegalArgumentException(ANSWER_CAN_NOT_BE_NULL);
        }
        if (answers.contains(answer)) {
            throw new IllegalArgumentException(DUPLICATE_ANSWER);
        }
        answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getWriter() {
        return this.writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public QuestionAnswers getQuestionAnswers() {
        return new QuestionAnswers(answers);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(NO_DELETE_PERMISSION);
        }
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.addAll(deleteAnswers(loginUser));
        deleteHistories.add(deleteQuestion(loginUser));
        return deleteHistories;
    }

    private DeleteHistory deleteQuestion(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(NO_DELETE_PERMISSION);
        }
        setDeleted(true);
        return new DeleteHistory(ContentType.QUESTION, id, getWriter(), LocalDateTime.now());
    }

    private List<DeleteHistory> deleteAnswers(User loginUser) throws CannotDeleteException {
        return getQuestionAnswers().delete(loginUser);
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
