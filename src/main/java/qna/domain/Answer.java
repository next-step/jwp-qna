package qna.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {
    public static final String ERROR_WRITTEN_BY_SOMEONE_ELSE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
    public static final boolean DELETE = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Contents contents;

    @Embedded
    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = new Contents(contents);
    }

    protected Answer() {
    }

    public void delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ERROR_WRITTEN_BY_SOMEONE_ELSE);
        }

        setDeleted(DELETE);
    }

    public Long getId() {
        return id;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents.getContents();
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void setDeleted(boolean deleted) {
        this.deleted = this.deleted.delete();
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answer answer = (Answer)o;
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(contents,
            answer.contents) && Objects.equals(question, answer.question) && Objects.equals(writer,
            answer.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, deleted, question, writer);
    }
}
