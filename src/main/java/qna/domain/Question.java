package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

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

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.toQuestion(this);
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

    public Answers getAnswers() {
        return answers;
    }

    public DeleteHistories delete(User loginUser) {
        validation(loginUser);
        delete();
        return DeleteHistories.fromQuestion(this);
    }

    public List<DeleteHistory> getDeleteHistoriesOfAnswers() {
        return answers.getDeleteHistories();
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer +
                ", deleted=" + deleted +
                '}';
    }

    private void validation(User loginUser) {
        validateOwner(loginUser);
        answers.validateAnswers(writer);
    }

    private void validateOwner(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ErrorMessage.DELETE_QUESTION_NOT_ALLOWED);
        }
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    private void delete() {
        this.deleted = true;
        answers.deleteAnswers();
    }
}
