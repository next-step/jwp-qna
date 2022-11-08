package qna.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static qna.constant.Message.NOT_VALID_DELETE_QUESTION_AUTH;
import static qna.constant.Message.NOT_VALID_DELETE_QUESTION_WITH_ANSWER;

@Entity
@Table(name = "question")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE question SET deleted = true WHERE id = ?")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)      // 외래키를 가지는 다 쪽으로 주인
    @JoinColumn(name = "writer_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
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

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
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

    public void updateContents(String contents) {
        this.contents = contents;
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
