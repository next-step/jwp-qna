package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseDateTimeEntity {
    private static final boolean CONTENT_DELETED_FLAG = true;
    private static final String EXCEPTION_MESSAGE_FOR_CANNOT_DELETE = "질문을 삭제할 권한이 없습니다.";
    private static final String EXCEPTION_MESSAGE_FOR_DUPLICATION_QUESTION = "이미 다른 질문에 달려있는 답글입니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;

    public Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        validateWriter(writer);
        validateQuestion(question);
        this.id = id;
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    private static void validateWriter(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
    }

    private static void validateQuestion(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    public boolean isOwner(User writer) {
        return this.writer.equalsNameAndEmail(writer);
    }

    public void toQuestion(Question question) {
        isAssignableQuestion();
        validateQuestion(question);
        this.question = question;
    }

    private void isAssignableQuestion() {
        if (!Objects.isNull(this.question)) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_FOR_DUPLICATION_QUESTION);
        }
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return this.writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory delete(User loginUser) throws CannotDeleteException {
        validateSameUser(loginUser);
        setDeleted(CONTENT_DELETED_FLAG);
        return new DeleteHistory(ContentType.ANSWER, id, loginUser, LocalDateTime.now());
    }

    private void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private void validateSameUser(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(EXCEPTION_MESSAGE_FOR_CANNOT_DELETE);
        }
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(getId(), answer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
