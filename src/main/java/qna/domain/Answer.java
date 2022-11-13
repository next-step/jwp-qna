package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Embedded
    @AttributeOverride(name = "content", column = @Column(name = "contents"))
    private Contents contents;

    @Embedded
    @AttributeOverride(name = "deleted", column = @Column(name = "deleted", nullable = false))
    private DeleteFlag deleted = DeleteFlag.notDeleted();

    public Answer(User writer, Question question, Contents contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, Contents contents) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User user) {
        this.writer = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Contents getContents() {
        return contents;
    }

    public void updateContents(Contents contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted.getDeleted();
    }

    public void deleteStatus(DeleteFlag deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAt();
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", user=" + writer.getUserId() +
                ", question=" + question.getTitle().getName() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return id.equals(answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }
}
