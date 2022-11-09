package qna.domain;

import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;
import qna.exception.type.QuestionExceptionType;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "answer")
public class Answer extends BaseTimeEntity {
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

    public User getWriter() {
        return writer;
    }

    public Question getQuestion() {
        return question;
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
                ", writer=" + writer +
                ", question=" + question +
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
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(writer, answer.writer) && Objects.equals(question, answer.question) && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }

    public DeleteHistory deleteIfValid(User loginUser) {
        validCheckDeleteAnswer(loginUser);
        this.deleted = true;

        return DeleteHistory.of(ContentType.ANSWER, this.question);
    }

    private void validCheckDeleteAnswer(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(QuestionExceptionType.DIFFERENT_USER_ANSWER_PRESENT.getMessage());
        }
    }
}
