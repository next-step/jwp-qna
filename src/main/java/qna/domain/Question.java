package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {
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
    @JoinColumn(name = "writer_id")
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {

    }

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

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
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
        validate(loginUser);
        this.deleted = true;

        DeleteHistories deleteHistoriesForQuestion = new DeleteHistories(DeleteHistory.ofQuestion(this));
        DeleteHistories deleteHistoriesForAnswers = new DeleteHistories(answers.delete(writer));
        return DeleteHistories.merge(deleteHistoriesForQuestion, deleteHistoriesForAnswers);
    }

    private void validate(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }
}
