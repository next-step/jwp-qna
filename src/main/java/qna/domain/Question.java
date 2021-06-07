package qna.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import qna.exceptions.CannotDeleteException;
import qna.exceptions.UnAuthorizedException;
import qna.validators.StringValidator;

@Table
@Entity
@Where(clause = "deleted=false")
public class Question extends BaseEntity {

    private static final int TITLE_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = TITLE_LENGTH, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        StringValidator.validate(title, TITLE_LENGTH);

        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return Objects.equals(this.writer.getId(), writer.getId());
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void editTitle(String title) {
        StringValidator.validate(title, TITLE_LENGTH);

        this.title = title;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistories delete(User writer) throws CannotDeleteException {
        if (!isOwner(writer)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        DeleteHistories deleteHistories = answers.delete(writer);
        deleteHistories.add(new DeleteHistory(this, writer));

        this.deleted = true;

        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return answers.getAnswers();
    }

    @Override
    public String toString() {
        return "Question{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", contents='" + contents + '\''
            + ", writerId=" + writer.getId()
            + ", deleted=" + deleted
            + ", createdAt=" + getCreatedAt()
            + ", updatedAt=" + getUpdatedAt()
            + '}';
    }
}
