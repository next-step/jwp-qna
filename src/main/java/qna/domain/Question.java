package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    private boolean deleted = false;

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new LinkedList<>();

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
        return this.writer.equals(writer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        if (answer.getQuestion() != this) {
            answer.toQuestion(this);
        }
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
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

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public List<DeleteHistory> deleteBy(User request) {
        confirmAuthority(request);
        confirmAnswer();

        return createDeletionHistory();
    }

    public void confirmAuthority(User request) {
        if (!isOwner(request)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void confirmAnswer() {
        for (Answer answer : getAnswers()) {
            if (!answer.isOwner(writer)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory setDeleted(boolean deleted) {
        this.deleted = deleted;
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
    }

    public List<DeleteHistory> createDeletionHistory() {
        List<DeleteHistory> histories = new ArrayList<>();
        histories.add(setDeleted(true));

        for (Answer answer : getAnswers()) {
            histories.add(answer.setDeleted(true));
        }

        return histories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted
                && Objects.equals(id, question.id)
                && Objects.equals(writer, question.writer)
                && Objects.equals(title, question.title)
                && Objects.equals(contents, question.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, title, contents, deleted);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", writer=" + writer +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
