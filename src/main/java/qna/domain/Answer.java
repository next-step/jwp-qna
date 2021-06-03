package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer extends BaseTimeEntity {

    private static final String CHECK_ANSWER_AUTHORITY = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    User writer;

    protected Answer() {}

    private Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public static Answer of(User writer, Question question, String contents) {
        return new Answer(null, writer, question, contents);
    }

    public static Answer of(Long id, User writer, Question question, String contents) {
        return new Answer(id, writer, question, contents);
    }

    public Long getId() {
        return this.id;
    }

    public void toQuestion(Question question) {
        question.addAnswer(this);
        this.question = question;
    }

    public boolean hasQuestion() {
        return question != null && question.getId() != null;
    }

    public boolean hasWriter() {
        return writer != null && writer.getId() != null;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public DeleteHistory deleteByOwner(User loginUser) {
        if (!writer.equals(loginUser)) {
            throw new CannotDeleteException(CHECK_ANSWER_AUTHORITY);
        }
        this.deleted = true;
        return DeleteHistory.of(ContentType.ANSWER, this.id, this.writer);
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public DeleteHistory createDeleteHistory() {
        return DeleteHistory.of(ContentType.ANSWER, this.id, writer);
    }

}
