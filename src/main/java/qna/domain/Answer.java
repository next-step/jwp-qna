package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column
    private Long questionId;

    @ManyToOne
    @JoinColumn(name ="writer_id")
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

        this.writer = writer;
        this.questionId = question.getId();
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.questionId = question.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getWriterId() {
        return writer.getId();
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

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.getId() +
                ", questionId=" + questionId +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
