package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Lob
    @Column(name = "contents")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers;

    protected Question() {

    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        LocalDateTime deletedAt = LocalDateTime.now();

        List<DeleteHistory> deleteHistories = new ArrayList<>();

        deleteHistories.add(deleteQuestion(loginUser, deletedAt));
        deleteHistories.addAll(deleteAnswers(loginUser, deletedAt));

        return deleteHistories;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    private List<DeleteHistory> deleteAnswers(User loginUser, LocalDateTime deletedAt) throws CannotDeleteException {
        return this.answers.delete(loginUser, deletedAt);
    }

    private DeleteHistory deleteQuestion(User loginUser, LocalDateTime deletedAt) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        this.deleted = true;
        this.setUpdatedAt(deletedAt);

        return new DeleteHistory(ContentType.QUESTION, this.id, this.writer, deletedAt);
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

    public User getWriter() {
        return writer;
    }

    public void setWriterId(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public Answers getAnswers() {
        return answers;
    }

    public void setAnswers(Answers answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        if (this.id != null && Objects.equals(getId(), question.getId())) return true;
        return isDeleted() == question.isDeleted() && Objects.equals(getId(), question.getId()) && Objects.equals(getTitle(), question.getTitle()) && Objects.equals(getContents(), question.getContents()) && Objects.equals(getWriter(), question.getWriter()) && Objects.equals(getAnswers(), question.getAnswers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getContents(), getWriter(), isDeleted(), getAnswers());
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
