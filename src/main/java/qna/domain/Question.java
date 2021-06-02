package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "question")
@Entity
public class Question extends QnaAbstract {
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    //@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @Embedded
    private Answers answers = new Answers();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

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

    public Answers getAnswers() {
        return this.answers;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
        answer.toQuestion(this);
    }
    public List<DeleteHistory> checkAuthorityDeleteQuestion(User deleter) throws CannotDeleteException {
        if (!isOwner(deleter)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        answers.checkAuthorityDeleteAnswers(deleter);
        return setDeletedQuestionAndAnswers();
    }

    private List<DeleteHistory> setDeletedQuestionAndAnswers() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(executeDeleted());
        deleteHistories.addAll(answers.executeDeleted());
        return deleteHistories;
    }

    public DeleteHistory executeDeleted() {
        this.deleted = true;
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
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
        return this.writer;
    }

    public void setWriterId(User writerId) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
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
}
