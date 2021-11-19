package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private QuestionTitle title;

    @Embedded
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private final Deleted deleted = new Deleted();

    @Embedded
    private final Answers answers = new Answers();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = new QuestionTitle(title);
        this.contents = new Contents(contents);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public List<Answer> getAnswers() {
        return answers.getAnswers();
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
        answer.toQuestion(this);
    }

    public void removeAnswer(Answer answer) {
        answer.removeQuestion();
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents.getContents();
    }

    public void changeContents(String contents) {
        this.contents.changeContents(contents);
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted.getDeleted();
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        validateOwner(loginUser);
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteQuestion(deleteHistories);
        deleteAnswers(loginUser, deleteHistories);
        return deleteHistories;
    }

    private void validateOwner(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private void deleteQuestion(List<DeleteHistory> deleteHistories) {
        this.deleted.delete();
        deleteHistories.add(DeleteHistory.ofQuestion(id, writer));
    }

    private void deleteAnswers(User loginUser, List<DeleteHistory> deleteHistories) throws CannotDeleteException {
        deleteHistories.addAll(answers.deleteAnswers(loginUser));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getTitle() {
        return title.getTitle();
    }
}
