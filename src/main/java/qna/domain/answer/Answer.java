package qna.domain.answer;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.*;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Answer extends DateTimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Embedded
    private Contents contents;

    @Embedded
    private Deleted deleted;

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, new Contents(contents));
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this(id, writer, question, new Contents(contents));
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

    public Answer() {

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

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void setDeleted(boolean deleted) {
        this.deleted = new Deleted(deleted);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.getId() +
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
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(writer, answer.writer) && Objects.equals(question, answer.question) && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }

    public DeleteHistory deletedBy(User user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        this.setDeleted(true);
        return DeleteHistory.ofAnswer(id, user, LocalDateTime.now());
    }
}
