package qna.domain;

import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
public class Question extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    @Column(nullable = true)
    private String contents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private final Answers answers = new Answers();

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() { }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validateDelete(loginUser);
        this.deleted = true;
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, getWriter(), LocalDateTime.now()));
        deleteHistories.add(answers.deleteAll(loginUser));
        return deleteHistories;
    }

    private void validateDelete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (answer.getQuestion() == null) {
            answer.toQuestion(this);
        }
        answers.add(answer);
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

    public List<Answer> getAnswers() {
        return answers.get();
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
                ", writerId=" + writer.getUserId() +
                ", deleted=" + deleted +
                '}';
    }
}
