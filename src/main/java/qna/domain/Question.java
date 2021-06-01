package qna.domain;

import org.springframework.lang.NonNull;
import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NonNull
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"), name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Answer> wroteAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Answer> wroteQuestions  = new ArrayList<>();

    @OneToMany(mappedBy = "deletedBy")
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    private boolean deleted = false;

    protected Question() {}

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

    public User getWriter() {
        return writer;
    }

    public void setWriterId(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        this.deleted = true;
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addDeleteHistory(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        DeleteHistories answerDeleteHistories = getAnswers().deleteAnswers(loginUser);
        return deleteHistories.concat(answerDeleteHistories);
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

    private Answers getAnswers() {
        return new Answers(answers);
    }
}
