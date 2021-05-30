package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.vo.Contents;
import qna.domain.vo.Deleted;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"), name = "writer_id")
    private User writer;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"), name = "question_id")
    private Question question;

    @Embedded
    private Contents contents;

    @Embedded
    private Deleted deleted = Deleted.FALSE;

    protected Answer() {
    }

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
        this.contents = Contents.of(contents);
        question.addAnswer(this);
    }

    public boolean isOwner(User writer) {
        return writer.equals(this.writer);
    }

    public void toQuestion(Question question) {
        if (this.question != null) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        question.getAnswers().add(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Contents getContents() {
        return contents;
    }

    public void changeContents(Contents contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
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

    public DeleteHistory delete(User user) throws CannotDeleteException {
        if (!isOwner(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변은 삭제할 수 없습니다.");
        }

        this.deleted = Deleted.TRUE;

        return new DeleteHistory(ContentType.ANSWER, id, writer, LocalDateTime.now());
    }
}
