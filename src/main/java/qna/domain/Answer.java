package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_answer")
public class Answer extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted;

    protected Answer () { }

    public Answer(Long userId, Question question, String contents) {
        this(null, userId, question, contents);
    }

    public Answer(Long id, Long userId, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(userId)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.userId = userId;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long id() {
        return id;
    }

    public void id(Long id) {
        this.id = id;
    }

    public Long writerId() {
        return userId;
    }

    public Long questionId() {
        return question.id();
    }

    public void questionId(Question question) {
        this.question = question;
    }

    public String contents() {
        return contents;
    }

    public void contents(String contents) {
        this.contents = contents;
    }

    public boolean deleted() {
        return deleted;
    }

    public void deleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + userId +
                ", questionId=" + question.id() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
