package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
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
        toQuestion(question);
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        if (Objects.nonNull(this.question)) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        question.getAnswers().add(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return this.writer;
    }

    public Question getQuestion() {
        return this.question;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory delete(User loginUser) {
        validateIsOwner(loginUser);
        this.deleted = true;
        return new DeleteHistory(ContentType.ANSWER, this.id, this.writer, LocalDateTime.now());
    }

    private void validateIsOwner(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", writer=" + writer +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answer answer = (Answer)o;
        return Objects.equals(getId(), answer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
