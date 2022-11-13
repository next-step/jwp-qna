package qna.domain;

import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;
import qna.message.AnswerMessage;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_question"))
    private Question question;

    @Lob
    private String contents;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    protected Answer() {

    }

    public Answer (User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer (Long id, User writer, Question question, String contents) {
        validateWriter(writer);
        validateQuestion(question);

        this.id = id;
        this.writer = writer;
        toQuestion(question);
        this.contents = contents;
    }

    private void validateQuestion(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException(AnswerMessage.ERROR_QUESTION_SHOULD_BE_NOT_NULL.message());
        }
    }

    private void validateWriter(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException(AnswerMessage.ERROR_WRITER_SHOULD_BE_NOT_NULL.message());
        }
    }

    public Long getId() {
        return this.id;
    }

    public User writer() {
        return this.writer;
    }

    public boolean isOwner(User owner) {
        return this.writer.equals(owner);
    }

    public DeleteHistory delete() {
        this.deleted = true;
        return DeleteHistory.ofAnswer(this);
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void toQuestion(Question question) {
        if(question.equals(this.question)) {
            return;
        }
        this.question = question;
        question.addAnswer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (!Objects.equals(id, answer.id)) return false;
        if (!Objects.equals(writer, answer.writer)) return false;
        return Objects.equals(question, answer.question);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (writer != null ? writer.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }
}
