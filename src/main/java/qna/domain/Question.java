package qna.domain;

import qna.CannotDeleteException;
import qna.domain.wrappers.Answers;
import qna.domain.wrappers.Contents;
import qna.domain.wrappers.Deleted;
import qna.domain.wrappers.Title;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void delete() {
        this.deleted = Deleted.createByDelete();
    }

    public boolean isContainAnswer(Answer answer) {
        return answers.contains(answer);
    }

    public void checkValidSameUserByQuestionWriter(User loginUser) throws CannotDeleteException {
        if (!this.writer.isSameUser(loginUser)) {
            throw new CannotDeleteException(AUTH_ERROR_MESSAGE);
        }
    }

    public void checkValidPossibleDeleteAnswers(User loginUser) throws CannotDeleteException {
        if (!answers.isEmpty() && !answers.isAllSameWriter(loginUser)) {
            throw new CannotDeleteException(NOT_MATCH_ANSWERS_WRITER_ERROR_MESSAGE);
        }
        this.deleted = Deleted.createByDelete();
    }

    public List<DeleteHistory> createDeleteHistories(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(this.createDeleteHistory(loginUser));
        List<DeleteHistory> deleteHistoriesByAnswers = this.createDeleteHistoriesByAnswers(loginUser);
        deleteHistories.addAll(deleteHistoriesByAnswers);
        return deleteHistories;
    }

    private List<DeleteHistory> createDeleteHistoriesByAnswers(User loginUser) {
        if (!answers.isEmpty()) {
            return answers.createDeleteHistories(loginUser);
        }
        return Collections.emptyList();
    }

    private DeleteHistory createDeleteHistory(User loginUser) {
        return DeleteHistory.create(QUESTION_CONTENT_TYPE, id, loginUser);
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
