package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, length = 100)
    private String title;

    @Lob
    @Column(updatable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = Answers.create();

    @Column(nullable = false)
    private boolean deleted = false;

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        validateTitle(title);
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    protected Question() {
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
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

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        validateOwner(user);
        List<DeleteHistory> deleteHistories = createDeleteHistories(user);
        deleteHistories.addAll(answers.delete(user));
        deleted = true;
        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", writer=" + writer +
            ", deleted=" + deleted +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return deleted == question.deleted && Objects.equals(id, question.id)
            && Objects.equals(title, question.title) && Objects.equals(contents, question.contents)
            && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted);
    }

    private boolean isNotOwner(User writer) {
        return !this.writer.equals(writer);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("'title' must not be empty");
        }
    }

    private List<DeleteHistory> createDeleteHistories(User user) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.ofQuestion(id, user));
        return deleteHistories;
    }

    private void validateOwner(User user) throws CannotDeleteException {
        if (isNotOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }
}
