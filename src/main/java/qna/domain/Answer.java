package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User writeBy;
    @ManyToOne
    private Question question;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {

    }

    public Answer(User writeBy, Question question, String contents) {
        this(null, writeBy, question, contents);
    }

    public Answer(Long id, User writeBy, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writeBy)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writeBy = writeBy;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writeBy.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public void delete() {
        this.deleted = true;
    }

    public Long getId() {
        return id;
    }

    public User getWriteBy() {
        return writeBy;
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
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writeBy.getId() +
                ", questionId=" + question.getId() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
