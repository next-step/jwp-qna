package qna.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "answer")
@SQLDelete(sql = "UPDATE answer SET deleted = ture WHERE id = ?")
@Where(clause = "deleted = false")
public class Answer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name="writer_id")
    private Long writerId;

    @Column(name="contents")
    @Lob
    private String contents;

    @Column(name="deleted", nullable = false)
    private boolean deleted = false;

    @JoinColumn(name="question_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    @ManyToOne()
    private Question question;

    @JoinColumn(name="writer_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    @ManyToOne()
    private User writer;


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

        this.writerId = writer.getId();
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public Long getWriterId() {
        return writerId;
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writerId +
                ", questionId=" + question.getId() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
