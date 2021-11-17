package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Embedded
    private Contents contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    Answer(User writer, Question question, String contents) {
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
        this.contents = Contents.from(contents);
        question.addAnswer(this);
    }

    public boolean isOwner(User writer) {
        return this.getWriterId().equals(writer.getId());
    }

    public DeleteHistory delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        setDeleted(true);

        return toDeleteHistory();
    }

    private DeleteHistory toDeleteHistory() {
        if (deleted) {
            return new DeleteHistory(ContentType.ANSWER, id, writer);
        }

        throw new IllegalStateException("삭제시에만 기록을 남길 수 있습니다.");
    }

    public Long getId() {
        return id;
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public User getWriter() {
        return writer;
    }

    void setWriter(User writer) {
        this.writer = writer;
    }

    public Question getQuestion() {
        return question;
    }

    void setQuestion(Question question) {
        if (Objects.nonNull(this.question)) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        question.addAnswer(this);
    }

    public Contents getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.getId() +
                ", questionId=" + question.getId() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
