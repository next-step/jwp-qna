package qna.domain;

import org.hibernate.annotations.Where;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "answer")
@Entity
public class Answer extends BaseEntity{

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    @ManyToOne
    private User writer;

    @Where(clause = "deleted = false")
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Lob
    private String contents;

    private boolean deleted = false;

    protected Answer(){}

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        super(id);

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

    public boolean isOwner(User writer) {
        return this.writer != null && this.writer.getId().equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public DeleteHistory delete(){
        this.deleted = true;
        return new DeleteHistory(ContentType.ANSWER, this.getId(), this.getWriter(), LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + getId() +
                ", writerId=" + this.writer.getId() +
                ", questionId=" + this.question.getId() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
