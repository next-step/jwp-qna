package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
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
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
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

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Question getQuestion() {
        return this.question;
    }
}
