package qna.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "WRITER_ID", foreignKey = @ForeignKey(name = "FK_Answer_User"))
    private User writer;

    @ManyToOne(fetch = LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "QUESTION_ID", foreignKey = @ForeignKey(name = "FK_Answer_Question"))
    private Question question;
    @Lob
    private String contents;

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

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.question = question;
    }


    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
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

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" + "id=" + id + ", writerId=" + writer.getId() + ", questionId=" + question.getId() + ", contents='" + contents + '\''
            + ", deleted=" + deleted + '}';
    }
}
