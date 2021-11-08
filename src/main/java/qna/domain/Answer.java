package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id", updatable = false, foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    @Column(updatable = false)
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    private Answer(Long id, User writer, Question question, String contents) {
        validateNonNull(writer);
        validateNonNull(question);
        this.id = id;
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    protected Answer() {
    }

    public static Answer of(Long id, User writer, Question question, String contents) {
        return new Answer(id, writer, question, contents);
    }

    public static Answer of(User writer, Question question, String contents) {
        return of(null, writer, question, contents);
    }

    boolean isNotOwner(User writer) {
        return !this.writer.equals(writer);
    }

    DeleteHistory delete() {
        deleted = true;
        return DeleteHistory.ofAnswer(id, writer);
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
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(writer,
            answer.writer) && Objects.equals(question, answer.question) && Objects.equals(contents,
            answer.contents);
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

    private void validateNonNull(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    private void validateNonNull(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
    }
}
