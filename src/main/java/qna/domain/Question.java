package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    public Question() { }

    public Question(String title, String contents) {
        this(title, null, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = null;
    }

    public Question(String title, User writer, String contents) {
        this(null, title, writer, contents);
    }

    public Question(Long id, String title, User writer, String contents) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.contents = contents;
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validateUserForDelete(loginUser);

        DeleteHistories deleteHistories = new DeleteHistories();
        this.setDeleted(true);

        deleteHistories.add(this.deleteHistory());

        Answers answers = new Answers(this.answers);
        deleteHistories.addAll(answers.deleteAllHistory());

        return deleteHistories;
    }

    protected DeleteHistory deleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
    }

    private void validateUserForDelete(User loginUser) throws CannotDeleteException {
         validateQuestionProprietary(loginUser);
         validateAnswersProprietary(loginUser);
    }

    protected void validateQuestionProprietary(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    protected void validateAnswersProprietary(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }
    }

    public Question writtenBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
        answer.toQuestion(this);
    }

    public void setWriter(User user) {
        this.writer = user;
    }

    public List<Answer> getAnswers() {
        return answers;
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted
                && Objects.equals(id, question.id)
                && Objects.equals(title, question.title)
                && Objects.equals(contents, question.contents)
                && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted);
    }

}
