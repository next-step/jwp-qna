package qna.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "question")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE question SET deleted = true WHERE id = ?")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;
    @Embedded
    private Answers answers = new Answers();
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writeBy;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

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
        this.writeBy = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writeBy.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.updateQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public Answers getAnswers() {
        return answers;
    }

    public User getWriteBy() {
        return writeBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        valid(loginUser);
        this.deleted = true;
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(this, loginUser);
        DeleteHistories deleteHistories = new DeleteHistories(deleteHistory);
        deleteHistories.add(answers.removeAll(loginUser));
        return deleteHistories;
    }

    private void valid(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", writerId=" + writeBy.getId() +
            ", deleted=" + deleted +
            '}';
    }
}
