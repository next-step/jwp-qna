package qna.domain;

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
import qna.constant.ErrMsg;
import qna.exception.CannotDeleteException;
import qna.exception.CannotUpdateException;

@Entity
public class Question extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;

    private boolean deleted = false;

    @Embedded
    private Answers answers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    protected Question() {

    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (!answer.getQuestion().equals(this)) {
            throw new CannotUpdateException(ErrMsg.CANNOT_ADD_ANSWER_TO_WRONG_QUESTION);
        }
        answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Answers getAnswers() {
        return answers;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validateDeletable(loginUser);
        DeleteHistories answerDeleteHistories = answers.deleteAll(loginUser);
        DeleteHistory questionDeleteHistory = DeleteHistory.ofQuestion(this);
        this.deleted = true;
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(questionDeleteHistory);
        deleteHistories.addAll(answerDeleteHistories);
        return deleteHistories;
    }

    private void validateDeletable(User loginUser) {
        if (this.deleted) {
            throw new CannotDeleteException(ErrMsg.CANNOT_DELETE_ALREADY_DELETED);
        }
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(ErrMsg.CANNOT_DELETE_WRONG_USER);
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", writer=" + writer.toString() +
                '}';
    }
}
