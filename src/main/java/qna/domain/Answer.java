package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.wrap.BigContents;
import qna.domain.wrap.Deletion;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    private BigContents contents;

    private Deletion deleted = new Deletion(false);

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this(id, writer, question, new BigContents(contents));
    }

    public Answer(Long id, User writer, Question question, BigContents contents) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        } else if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.id = id;
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer == writer;
    }

    public boolean isAnswerOf(Question question) {
        return this.question == question;
    }

    protected void toQuestion(Question question) {
        this.question = question;
    }

    protected DeleteHistory delete(User deleter) throws CannotDeleteException {
        if(isDeleted()) {
            throw new IllegalStateException("이미 삭제가 되어있습니다.");
        }else if (!isOwner(deleter)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        this.deleted = deleted.delete();

        return new DeleteHistory(ContentType.ANSWER, id, deleter, LocalDateTime.now());
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + isDeleted() +
                '}';
    }
}
