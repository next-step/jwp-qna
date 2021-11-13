package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private AnswerContents contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    public void setQuestion(Question question) {
        throwOnNotFoundQuestion(question);

        if (this.question != null) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;

        if (!question.getAnswers().contains(this)) {
            question.getAnswers().add(this);
        }
    }

    private void throwOnNotFoundQuestion(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    protected Answer() {

    }

    private Answer(Long id, AnswerContents contents, boolean deleted, Question question, User writer) {
        this.id = id;
        this.contents = contents;
        this.deleted = deleted;
        this.question = question;
        this.writer = writer;
    }

    public static Answer of(User writer, String contents) {
        return of(null, writer, contents);
    }

    public static Answer of(Long id, User writer, String contents) {
        throwOnUnAuthorizedWriter(writer);

        return new Answer(id, AnswerContents.of(contents), false, null, writer);
    }

    private static void throwOnUnAuthorizedWriter(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
    }

    public DeleteHistory delete(User deleter) {
        this.deleted = true;

        return DeleteHistory.ofAnswer(deleter, this);
    }

    public boolean isOwner(User user) {
        return this.writer.equals(user);
    }

    public Long getId() {
        return id;
    }

    public AnswerContents getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Question getQuestion() {
        return question;
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Answer answer = (Answer)obj;
        return id.equals(answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
