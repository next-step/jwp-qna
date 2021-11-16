package qna.domain.answer;

import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.BaseTimeEntity;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    private String contents;

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
        this.question = question;
        this.contents = contents;
    }

    public Answer(Long id, User writer, Question question, String contents, boolean deleted) {
        this(id, writer, question, contents);
        this.deleted = deleted;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer.getId());
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

    public Long getQuestionId() {
        return question.getId();
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

    public void delete() {
        this.deleted = true;
    }

    public void userClear() {
        this.writer = null;
    }

    public void updateWriter(User target) {
        this.writer.update(target);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + (Objects.isNull(writer) ? null : writer.getId()) +
                ", questionId=" + (Objects.isNull(question) ? null : question.getId()) +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
