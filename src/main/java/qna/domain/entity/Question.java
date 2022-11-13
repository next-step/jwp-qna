package qna.domain.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qna.CannotDeleteException;
import qna.domain.ContentType;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Question extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "contents")
    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User user;

    @Embedded
    private Answers answers = new Answers();

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Question() {
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
        this.user = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.user.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public Answers getAnswers() {
        return answers;
    }

    public void setAnswers(Answers answers) {
        this.answers = answers;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        validCheckOwner(loginUser);

        List<DeleteHistory> deleteHistories = this.answers.delete(loginUser);
        return deleteQuestionAndSaveHistory(deleteHistories);
    }

    private List<DeleteHistory> deleteQuestionAndSaveHistory(List<DeleteHistory> deleteHistories) {
        setDeleted(true);
        deleteHistories.add(0, new DeleteHistory(ContentType.QUESTION, id, user));

        return deleteHistories;
    }

    private void validCheckOwner(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question that = (Question) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(contents, that.contents) &&
                Objects.equals(user, that.user);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
