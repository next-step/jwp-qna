package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {
    public static final String ERROR_PERMISSION_TO_DELETE = "질문을 삭제할 권한이 없습니다.";
    public static final boolean DELETE = true;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Contents contents;

    @Embedded
    private Deleted deleted;

    @Embedded
    private Title title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    Answers answers;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.answers = new Answers();
        this.deleted = new Deleted();
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public DeleteHistorys delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ERROR_PERMISSION_TO_DELETE);
        }
        setDeleted(DELETE);

        DeleteHistory thisDeleteHistory =
            new DeleteHistory(ContentType.QUESTION, loginUser.getId(), this.getWriter(), LocalDateTime.now());
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(thisDeleteHistory);
        deleteHistories.addAll(answers.delete(loginUser));
        return new DeleteHistorys(deleteHistories);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.setQuestion(this);
        answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title.getTitle();
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void setDeleted(boolean deleted) {
        this.deleted = new Deleted(deleted);
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents.getContents();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Question question = (Question)o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(
            contents, question.contents) && Objects.equals(title, question.title) && Objects.equals(
            writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, deleted, title, writer);
    }
}
