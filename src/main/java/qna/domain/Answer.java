package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.wrappers.Contents;
import qna.domain.wrappers.Deleted;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
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

    @Embedded
    private Contents contents = new Contents();

    @Embedded
    private Deleted deleted = new Deleted();

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
        this.contents = new Contents(contents);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public User writer() {
        return writer;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void delete() {
        this.deleted = Deleted.createByDelete();
    }

    public boolean isWriterUser(User user) {
        return writer.isSameUser(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id) &&
                Objects.equals(writer, answer.writer) &&
                Objects.equals(question, answer.question) &&
                Objects.equals(contents, answer.contents) &&
                Objects.equals(deleted, answer.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.id() +
                ", questionId=" + question.id() +
                ", " + contents.toString() + '\'' +
                ", " + deleted.toString() +
                '}';
    }
}
