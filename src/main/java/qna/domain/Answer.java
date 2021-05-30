package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.wrapper.Deleted;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey=@ForeignKey(name="fk_answer_to_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey=@ForeignKey(name="fk_answer_to_question"))
    private Question question;

    @Lob
    private String contents;

    @Embedded
    private Deleted deleted;

    public Answer() { }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.id = id;
        this.writer = writer;
        this.question = question;
        this.contents = contents;
        this.deleted = new Deleted();
    }

    public DeleteHistory deleteAndReturnHistory() {
        this.delete();

        return new DeleteHistory(ContentType.ANSWER, this.id, this.writer, LocalDateTime.now());
    }

    public void delete() {
        this.deleted.setTrue();
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

    public User getWriter() {
        return writer;
    }

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted.status();
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(writer, answer.writer) && Objects.equals(question, answer.question) && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
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

}
