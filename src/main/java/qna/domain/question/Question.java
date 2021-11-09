package qna.domain.question;

import qna.CannotDeleteException;
import qna.domain.Contents;
import qna.domain.DateTimeBaseEntity;
import qna.domain.Deleted;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;
import qna.domain.answer.Answer;
import qna.domain.answer.Answers;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Question extends DateTimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @Embedded
    private Answers answers = new Answers();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Deleted deleted = new Deleted();

    public Question(String title, String contents) {
        this(null, new Title(title), new Contents(contents));
    }

    public Question(Long id, String title, String contents) {
        this(id, new Title(title), new Contents(contents));
    }

    public Question(Long id, Title title, Contents contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question() {

    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Contents getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = new Deleted(deleted);
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(title, question.title) && Objects.equals(contents, question.contents) && Objects.equals(answers, question.answers) && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, answers, writer, deleted);
    }

    public DeleteHistory deletedBy(User user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("삭제 권한이 없습니다.");
        }
        this.setDeleted(true);
        return DeleteHistory.ofQuestion(id, user, LocalDateTime.now());
    }

}
