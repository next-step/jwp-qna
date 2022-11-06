package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne
    private User writeBy;
    @Column(nullable = false)
    private boolean deleted = false;
    @Embedded
    private Answers answers;

    protected Question() {

    }

    public Question(Long id, User writeBy, String title, String contents) {
        this.id = id;
        this.writeBy = writeBy;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers(new ArrayList<>());
    }

    public Question writeBy(User writer) {
        this.writeBy = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writeBy.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (answers.contains(answer)) {
            return;
        }
        answers.addAnswer(answer);
        answer.toQuestion(this);
    }

    public void removeAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            return;
        }
        answers.removeAnswer(answer);
        answer.toQuestion(null);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.deleted = true;

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.id, this.writeBy, LocalDateTime.now()));
        deleteHistories.addAll(answers.deleteAll(loginUser));

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

    public User getWriteBy() {
        return writeBy;
    }

    public boolean isDeleted() {
        return deleted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
