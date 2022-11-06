package qna.domain;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

@Entity
public class Answer extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name="fk_answer_to_question"))
    private Question question;
    @Embedded
    private Contents contents;
    @Column(nullable = false)
    private boolean deleted = false;

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
        validateQuestion(question);

        this.writer = writer;
        toQuestion(question);
        this.contents = Contents.of(contents);
    }

    private void validateQuestion(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException(ErrorCode.질문이_존재하지_않음.getErrorMessage());
        }
        if(question.isDeleted()) {
            throw new NotFoundException(ErrorCode.삭제된_질문에는_답변할_수_없음.getErrorMessage());
        }
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    private void toQuestion(Question question) {
        if(this.question != null) {
            this.question.removeAnswer(this);
        }
        this.question = question;
        question.addAnswer(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Contents getContents() {
        return this.contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void validateSameUser(User user) {
        if(!isOwner(user)) {
            throw new CannotDeleteException(ErrorCode.답변_중_다른_사람이_쓴_답변_있어_삭제_못함.getErrorMessage());
        }
    }

    private DeleteHistory changeDeleted(boolean deleted) {
        this.deleted = deleted;
        return DeleteHistory.ofAnswer(this.id, this.writer);
    }

    public DeleteHistory delete(User user) {
        validateSameUser(user);
        return changeDeleted(true);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", question=" + question +
                ", contents=" + contents +
                ", deleted=" + deleted +
                '}';
    }
}
