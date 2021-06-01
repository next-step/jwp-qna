package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;
import qna.domain.vo.Contents;
import qna.domain.vo.Deleted;
import qna.domain.vo.Title;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"), name = "writer_id")
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    @Embedded
    private Deleted deleted = Deleted.FALSE;

    public Question(String title, String contents) {
        this(null, title, contents, null);
    }

    public Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    public Question(Long id, String title, String contents, User writer) {
        this.id = id;
        this.title = Title.of(title);
        this.contents = Contents.of(contents);
        this.writer = writer;
    }

    protected Question() {
    }

    Question(String title, boolean deleted) {
        this.title = Title.of(title);
        this.deleted = Deleted.of(deleted);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public boolean isAnswersDeleted() {
        return answers.isDeleted();
    }

    Title getTitle() {
        return title;
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

    public DeleteHistories delete(User user, LocalDateTime deletedAt) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        deleted = Deleted.TRUE;
        DeleteHistory questionDeleteHistory = DeleteHistory.of(this, deletedAt);
        DeleteHistories questionDeleteHistories = DeleteHistories.of(questionDeleteHistory);
        DeleteHistories answerDeleteHistories = answers.delete(user, deletedAt);

        return questionDeleteHistories.concat(answerDeleteHistories);
    }

    Answers getAnswers() {
        return answers;
    }
}
