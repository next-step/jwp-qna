package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import qna.exception.CannotDeleteException;

@Entity
@Table(name = "question")
public class Question extends AuditTimeBaseEntity {
    @Embedded
    private Answers answers;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @Column(length = 100, nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this(id, contents, title, null, false);
    }

    private Question(Long id, String title, String contents, User writer, boolean deleted) {
        this(id, title, contents, writer, deleted, new Answers());
    }

    private Question(Long id, String title, String contents, User writer, boolean deleted, Answers answers) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = User.from(writer);
        this.deleted = deleted;
        this.answers = Answers.from(answers);
    }

    protected Question() {
    }

    public static Question from(Question question) {
        if (Objects.isNull(question)) {
            return null;
        }
        return new Question(
                question.id,
                question.title,
                question.contents,
                question.writer,
                question.deleted,
                question.answers);
    }

    public DeleteHistories delete(User user) throws CannotDeleteException {
        if (!isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        setDeleted(true);
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.of(ContentType.QUESTION, id, getWriter()));

        deleteHistories.addAll(answers.deleteAll(user));

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

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", title='" + title + '\'' +
                ", writer=" + writer +
                '}';
    }
}
