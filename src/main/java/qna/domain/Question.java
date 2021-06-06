package qna.domain;

import qna.CannotDeleteException;
import qna.domain.wrappers.Answers;
import qna.domain.wrappers.Contents;
import qna.domain.wrappers.Deleted;
import qna.domain.wrappers.Title;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    private static final String AUTH_ERROR_MESSAGE = "질문을 삭제할 권한이 없습니다.";
    private static final String NOT_MATCH_ANSWERS_WRITER_ERROR_MESSAGE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
    public static final ContentType QUESTION_CONTENT_TYPE = ContentType.QUESTION;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Deleted deleted = new Deleted();

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void addAnswer(Answer answer) {
        if (!isContainAnswer(answer)) {
            answers.addAnswer(answer);
            answer.toQuestion(this);
        }
    }

    public Long id() {
        return id;
    }

    public User writer() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public boolean isContainAnswer(Answer answer) {
        return answers.contains(answer);
    }

    public void checkPossibleDelete(User loginUser) throws CannotDeleteException {
        this.checkValidSameUserByQuestionWriter(loginUser);
        this.checkValidPossibleDeleteAnswers(loginUser);
    }

    public void delete() {
        this.deleted = Deleted.createByDelete();
        this.answers.delete();
    }

    public Answers answers() {
        return answers;
    }

    private void checkValidSameUserByQuestionWriter(User loginUser) throws CannotDeleteException {
        if (!this.writer.isSameUser(loginUser)) {
            throw new CannotDeleteException(AUTH_ERROR_MESSAGE);
        }
    }

    private void checkValidPossibleDeleteAnswers(User loginUser) throws CannotDeleteException {
        if (!answers.isEmpty() && !answers.isAllSameWriter(loginUser)) {
            throw new CannotDeleteException(NOT_MATCH_ANSWERS_WRITER_ERROR_MESSAGE);
        }
        this.deleted = Deleted.createByDelete();
    }

    @Override
    public String toString() {
        return "Question{" +
                ", " + id +
                ", " + title.toString() + '\'' +
                ", " + contents.toString() + '\'' +
                ", writerId=" + writer.id() +
                ", " + deleted.toString() +
                '}';
    }
}
