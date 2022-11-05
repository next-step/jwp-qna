package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends DeletableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Lob
    @Column(nullable = true)
    private String contents;

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

        this.contents = contents;

        toQuestion(question);
        setWriter(writer);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        if (Objects.nonNull(this.question)) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        if (!question.getAnswers().contains(this)) {
            question.getAnswers().add(this);
        }
    }

    public void setWriter(User writer) {
        if (Objects.nonNull(this.writer)) {
            this.writer.getAnswers().remove(this);
        }
        this.writer = writer;

        if (!this.writer.getAnswers().contains(this)) {
            this.writer.getAnswers().add(this);
        }
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public boolean isDeleted() {
        return super.isDeleted();
    }

    public void delete() {
        super.delete();
    }

    public void delete(User writer) {
        super.delete();
    }

    public void modify(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return this.contents;
    }

    public Question getQuestion() {
        return question;
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", writer=" + (Objects.isNull(writer) ? "" : writer.getUserId()) +
            ", question=" + question.getId() +
            ", contents='" + contents + '\'' +
            ", deleted=" + isDeleted() +
            '}';
    }
}
