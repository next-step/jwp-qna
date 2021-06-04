package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    private static final String AUTH_ERROR_MESSAGE = "질문을 삭제할 권한이 없습니다.";
    private static final String NOT_MATCH_ANSWERS_WRITER_ERROR_MESSAGE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers = new Answers();

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
        return deleted;
    }

    public void delete(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isContainAnswer(Answer answer) {
        return answers.contains(answer);
    }

    public boolean isSameByAnswersSize(int size) {
        return answers.isSameSize(size);
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
