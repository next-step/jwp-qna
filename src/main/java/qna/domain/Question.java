package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

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

    public List<Answer> getAnswers() {
        return this.answers;
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

    public List<DeleteHistory> checkDeleteQuestionAuthority(User deleter) throws CannotDeleteException {
        if (!isOwner(deleter)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        checkDeleteAnswersAuthority();
        return setDeletedQuestionAndAnswers(true);
    }

    private void checkDeleteAnswersAuthority() throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.checkDeleteAnswerAuthority(writer);
        }
    }

    private List<DeleteHistory> setDeletedQuestionAndAnswers(boolean deleted) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(setDeleted(deleted));
        answers.stream()
                .forEach(answer -> deleteHistories.add(answer.setDeleted(deleted)));
        return deleteHistories;
    }

    public DeleteHistory setDeleted(boolean deleted) {
        this.deleted = deleted;
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
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
