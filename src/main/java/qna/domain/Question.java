package qna.domain;

import qna.CannotDeleteException;
import qna.QnaExceptionType;

import javax.persistence.*;
import java.util.List;

import java.util.Objects;

@Entity
public class Question extends BaseTimeEntity {
    private static final int FIRST_INDEX = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Embedded
    private Answers answers = new Answers();
    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(final Long id, final String title, final String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question(final String title, final String contents) {
        this(null, title, contents);
    }

    public Question writeBy(final User writer) {
        this.writer = writer;
        return this;
    }

    public DeleteHistories delete(final User writer) throws CannotDeleteException {
        final DeleteHistory deleteHistory = deleteQuestion(writer);
        final List<DeleteHistory> deleteHistories = answers.deleteAll(writer);
        deleteHistories.add(FIRST_INDEX, deleteHistory);

        return DeleteHistories.toHistories(deleteHistories);
    }

    private DeleteHistory deleteQuestion(final User writer) throws CannotDeleteException {
        validateOwner(writer);
        deleted = true;

        return DeleteHistory.ofQuestion(id, writer);
    }

    private void validateOwner(User writer) throws CannotDeleteException {
        if (!writer.isOwner(this.writer)) {
            throw new CannotDeleteException(QnaExceptionType.NOT_AUTHOR_QUESTION);
        }
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);

        if (answer.getQuestion() != this) {
            answer.toQuestion(this);
        }
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public void updateWriter(User writer) {
        if (writer != null) {
            this.writer.addQuestion(this);
        }
        if (!writer.containQuestion(this)) {
            writer.addQuestion(this);
        }
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
                ", createAt=" + getCreateAt() +
                ", updateAt=" + getUpdateAt() +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Question question = (Question) o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(title, question.title) && Objects.equals(contents, question.contents) && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted);
    }
}
