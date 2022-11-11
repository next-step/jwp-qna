package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseDateTimeEntity {
    private static final boolean CONTENT_DELETED_FLAG = true;
    private static final String EXCEPTION_MESSAGE_FOR_CANNOT_DELETE = "질문을 삭제할 권한이 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Embedded
    private Answers answers;
    private boolean deleted = false;

    public Question() {
        this.answers = new Answers();
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

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Answer> answers() {
        return answers.answers();
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validateSameUser(loginUser);
        setDeleted(CONTENT_DELETED_FLAG);

        DeleteHistories deleteHistories = new DeleteHistories(new DeleteHistory(ContentType.QUESTION, id, loginUser, LocalDateTime.now()));
        deleteHistories.addAll(answers.delete(loginUser));

        return deleteHistories;
    }

    private void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private void validateSameUser(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(EXCEPTION_MESSAGE_FOR_CANNOT_DELETE);
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
                '}';
    }
}
