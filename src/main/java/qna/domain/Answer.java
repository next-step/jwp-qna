package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    @Lob
    @Column(name = "contents", columnDefinition = "clob")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Column(name = "deleted", columnDefinition = "boolean", nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException("답변은 로그인 하신 후에 작성 가능합니다.");
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException("답변을 할 질문이 없습니다.");
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public DeleteHistory delete(User loginUser) throws CannotDeleteException {
        answerAuthCheck(loginUser);
        setDeleted(true);
        return DeleteHistory.answerHistory(getId(), writer);
    }

    private void answerAuthCheck(User writer) throws CannotDeleteException {
        if (!isOwner(writer)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public DeleteHistory deleteHistory() {
        return new DeleteHistory(ContentType.ANSWER, this.id, this.writer, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", question=" + question +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(contents, answer.contents) && Objects.equals(writer, answer.writer) && Objects.equals(question, answer.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, writer, question, deleted);
    }
}
