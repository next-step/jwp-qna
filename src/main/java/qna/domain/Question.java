package qna.domain;

import qna.CannotDeleteException;
import qna.domain.wrap.BigContents;
import qna.domain.wrap.Deletion;
import qna.domain.wrap.Title;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Title title;

    private BigContents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    private Deletion deleted = new Deletion(false);

    private Answers answers = new Answers();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents, User.GUEST_USER);
    }

    public Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    public Question(Long id, String title, String contents) {
        this(id, title, contents, null);
    }

    public Question(Long id, String title, String contents, User writer) {
        this(id, new Title(title), new BigContents(contents), writer);
    }

    public Question(Long id, Title title, BigContents contents, User writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public DeleteHistories delete(User deleter) throws CannotDeleteException {
        if (isDeleted()) {
            throw new IllegalStateException("이미 삭제가 되어있습니다.");
        }
        if (!isOwner(deleter)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        DeleteHistories deleteHistories = deleteQuestion(deleter);

        return deleteHistories.addAll(answers.deleteAll(deleter));
    }

    public boolean isOwner(User writer) {
        return this.writer == writer;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public Answers getAnswers() {
        return answers;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    private DeleteHistories deleteQuestion(User deleter) {
        DeleteHistories deleteHistories = new DeleteHistories(
                new DeleteHistory(ContentType.QUESTION, id, deleter, LocalDateTime.now())
        );

        this.deleted = deleted.delete();

        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", deleted=" + isDeleted() +
                '}';
    }
}
