package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import static qna.domain.ContentType.ANSWER;

@Entity
public class Answer extends TimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Lob
    private String contents;
    @Column(nullable = false, columnDefinition = "bit")
    private boolean deleted = false;

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

    protected Answer() {

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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer.toString() +
                ", question=" + question.toString() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    public User getWriter() {
        return this.writer;
    }

    public DeleteHistory delete() {
        this.deleted = true;
        return new DeleteHistory(ANSWER, getId(), getWriter(), LocalDateTime.now());
    }

    public boolean isDeleted() {
        return deleted;
    }
}
