package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {
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
    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

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

    public boolean isOwner(User writer) {
        return this.writer.matchUserId(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public boolean checkQuestionDeleteAuth(User user) throws CannotDeleteException {
        if (!writer.matchUserId(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        return true;
    }

    public List<DeleteHistory> deleteQuestion() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = deleteAnswersBeforeDeleteQuestion();
        this.deleted = true;
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.id, this.writer, LocalDateTime.now()));
        return deleteHistories;
    }

    protected List<DeleteHistory> deleteAnswersBeforeDeleteQuestion() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        checkAnswersDeletable();
        for (Answer answer : answers) {
            answer.deleteAnswerIfDeletedFalse(deleteHistories);
        }
        return deleteHistories;
    }

    private void checkAnswersDeletable() throws CannotDeleteException {
        for (Answer answer : answers) {
            checkAnswerWriterMatch(answer);
        }
    }

    private void checkAnswerWriterMatch(Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(writer)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", deleted=" + deleted +
                //", answers=" + answers.hashCode() +
                '}';
    }
}
