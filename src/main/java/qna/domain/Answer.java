package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.field.Contents;
import qna.domain.field.Deleted;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Contents contents;

    @Embedded
    private Deleted deleted;

    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;
    
    // Arguments가 없는 Default Constructor 생성
    protected Answer() {}

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
        this.deleted = new Deleted(false);
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

    public void setId(Long id) {
        this.id = id;
    }

    public User getWriter() { return this.writer; }

    public String getContents() {
        return contents.getContents();
    }

    public void setContents(String contents) {
        this.contents = new Contents(contents);
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isDeleted() {
        return deleted.getDeleted();
    }

    public void setDeleted(boolean deleted) {
        this.deleted = new Deleted(deleted);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.toString() +
                ", questionId=" + question.toString() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

}
