package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import qna.exception.CannotDeleteException;

@Entity
public class Question extends BaseEntity {

    public static final String NO_PERMISSION_TO_DELETE_QUESTION = "질문을 삭제할 권한이 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    public Question(String title, String contents, User writer, boolean deleted) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.deleted = deleted;
    }

    public Question(Long id, String title, String contents, User writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    protected Question() {

    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(NO_PERMISSION_TO_DELETE_QUESTION);
        }
        deleted = true;

        DeleteHistories deleteHistories = new DeleteHistories(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        deleteHistories.addAll(getAnswers().deleteAnswers(loginUser));
        return deleteHistories;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
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

    public boolean isDeleted() {
        return deleted;
    }

    public User getWriter() {
        return writer;
    }

    public Answers getAnswers() {
        return new Answers(answers);
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
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer.getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", writer.id=" + writer.getId() +
            ", deleted=" + deleted +
            '}';
    }

    public void update(Question question) {
        this.title = question.getTitle();
        this.contents = question.getContents();
    }
}
