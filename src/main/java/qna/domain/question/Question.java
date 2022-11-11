package qna.domain.question;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.CannotDeleteException;
import qna.domain.BaseEntity;
import qna.domain.ContentType;
import qna.domain.content.Content;
import qna.domain.deleteHistory.DeleteHistory;
import qna.domain.user.User;
import qna.domain.answer.Answer;
import qna.domain.answer.Answers;
import qna.domain.content.Title;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static qna.constant.Message.NOT_VALID_DELETE_QUESTION_AUTH;

@Entity
@Table(name = "question")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE question SET deleted = true WHERE id = ?")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Embedded
    private Title title;
    @Embedded
    private Content content;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)      // 외래키를 가지는 다 쪽으로 주인
    @JoinColumn(name = "writer_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    public Question(Title title, Content content) {
        this(null, title, content);
    }

    public Question(Long id, Title title, Content content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
    }

    public Long getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Content getContents() {
        return content;
    }

    public User getWriter() {
        return writer;
    }

    public List<Answer> getAnswers() { return answers.getAnswers(); }

    public boolean isDeleted() {
        return deleted;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public void updateContents(Content content) {
        this.content = content;
    }

    public DeleteHistory createDeleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, this.id, this.writer, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + content + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }


    public void deleteByLoginUser(User loginUser) throws CannotDeleteException {
        checkWriter(loginUser);
        answers.validateDeleteAnswer(loginUser);
        updateDeleted(true);

        answers.deleteAll();
    }

    public void checkWriter(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(NOT_VALID_DELETE_QUESTION_AUTH);
        }
    }

    public boolean isContainAnswer(Answer answer) {
        return getAnswers().contains(answer);
    }
}
