package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import qna.CannotDeleteException;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "write_id")
    private User writer;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    Question(String title, String contents) {
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

    public void addAnswer(final Answer answer) {
        answers.add(answer);
        if (answer.getQuestion() != this) {
            answer.toQuestion(this);
        }
    }

    public void removeAnswer(final Answer answer) {
        answers.remove(answer);
    }

    public List<DeleteHistory> delete(final User writer) throws CannotDeleteException {
        canBeDeletedBy(writer);
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        this.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION,
                id,
                writer,
                LocalDateTime.now()));
        deleteHistories.addAll(answers.delete());
        return deleteHistories;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private void canBeDeletedBy(final User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        if (!answers.allWrittenBy(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", deleted=" + deleted +
                ", createdAt=" + super.getCreatedAt() +
                ", modifiedAt=" + super.getModifiedAt() +
                '}';
    }
}
