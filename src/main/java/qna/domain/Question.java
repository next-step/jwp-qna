package qna.domain;

import java.time.LocalDateTime;
import java.util.List;
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
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"), name = "question_id")
    private User writer;

    private boolean deleted = false;

    @Embedded
    private Answers answers;

    protected Question() {

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
        answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<DeleteHistory> deleteQuestion(final User loginUser) throws CannotDeleteException {
        validateDeleteQuestion(loginUser);
        validateDeleteAnswers(loginUser);
        return deleteAllAndGetDeleteHistories();
    }

    private void validateDeleteQuestion(final User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 수 있는 권한이 없습니다.");
        }
    }

    private void validateDeleteAnswers(final User loginUser) throws CannotDeleteException {
        if (answers.isNotWrittenUserInAnswers(loginUser)) {
            throw new CannotDeleteException("다른 사람이 작성한 답변이 있어서 질문을 삭제할 수 없습니다.");
        }
    }

    private List<DeleteHistory> deleteAllAndGetDeleteHistories() {
        DeleteHistories deleteHistories = new DeleteHistories();
        setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        answers.deleteAll().getDeleteHistories().forEach(deleteHistories::add);
        return deleteHistories.getDeleteHistories();
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
