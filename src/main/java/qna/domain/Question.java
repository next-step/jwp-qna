package qna.domain;

import qna.exception.CannotDeleteException;
import qna.message.QuestionMessage;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = Boolean.FALSE;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {

    }

    public Question(User writer, String title, String contents) {
        this(null, writer, title, contents);
    }

    public Question(Long id, User writer, String title, String contents) {
        validateTitle(title);
        validateContents(contents);

        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    private void validateTitle(String title) {
        if(Objects.isNull(title)) {
            throw new IllegalArgumentException(QuestionMessage.ERROR_TITLE_SHOULD_BE_NOT_NULL.message());
        }
    }

    private void validateContents(String contents) {
        if(Objects.isNull(contents)) {
            throw new IllegalArgumentException(QuestionMessage.ERROR_CONTENTS_SHOULD_BE_NOT_NULL.message());
        }
    }

    public Long getId() {
        return id;
    }

    public User writer() {
        return this.writer;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public int numberOfAnswers() {
        return this.answers.size();
    }

    public DeleteHistories delete(User owner) throws CannotDeleteException {
        validateIsDeletable();
        validateOwner(owner);
        this.deleted = true;

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(this));
        deleteHistories.addAll(this.answers.deleteAll(owner));
        return deleteHistories;
    }

    private void validateIsDeletable() {
        if(this.deleted) {
            throw new IllegalArgumentException(QuestionMessage.ERROR_ALREADY_IS_DELETED.message());
        }
    }

    private void validateOwner(User owner) throws CannotDeleteException {
        if (!this.isOwner(owner)) {
            throw new CannotDeleteException(QuestionMessage.ERROR_CAN_NOT_DELETE_IF_NOT_OWNER.message());
        }
    }

    public boolean isDeleted() {
        return this.deleted;
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
        return id != null ? id.hashCode() : 0;
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
