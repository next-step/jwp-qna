package qna.domain;

import java.util.List;
import java.util.Objects;
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
import qna.CannotDeleteException;
import qna.common.ErrorMessage;

@Entity
@Table
public class Question extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @Embedded
    private final Answers answers = new Answers();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public DeleteHistories delete(User loginUser) {
        validDeleted();
        validWriter(loginUser);
        DeleteHistory questionDeleteHistory = deleteQuestion(loginUser);

        if (answers.isEmpty()) {
            return DeleteHistories.of(questionDeleteHistory);
        }

        DeleteHistories deleteHistories = answers.delete(loginUser);
        deleteHistories.add(questionDeleteHistory);
        return deleteHistories;
    }

    private void validDeleted() {
        if (isDeleted()) {
            throw new IllegalStateException(ErrorMessage.ALREADY_DELETED);
        }
    }

    private void validWriter(User loginUser) {
        if (writer.isNotWriter(loginUser)) {
            throw new CannotDeleteException(ErrorMessage.NOT_PERMISSION_DELETE_QUESTION);
        }
    }

    private DeleteHistory deleteQuestion(User loginUser) {
        this.deleted = true;
        return DeleteHistory.questionOf(this.getId(), loginUser);
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
        answer.addQuestion(this);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getWriter() {
        return writer;
    }

    public List<Answer> getAnswers() {
        return answers.getAnswers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return deleted == question.deleted
                && Objects.equals(id, question.id)
                && Objects.equals(title, question.title)
                && Objects.equals(contents, question.contents)
                && Objects.equals(writer, question.writer)
                && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted, answers);
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
