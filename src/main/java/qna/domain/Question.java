package qna.domain;

import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    private static final String NOT_QUESTION_WRITER = "질문을 삭제할 권한이 없습니다.";

    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Lob
    @Column(name = "contents", columnDefinition = "CLOB")
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
    @Embedded
    private Answers answers = Answers.empty();

    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.addAnswer(answer);
    }

    public int answersSize() {
        return this.answers.size();
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(NOT_QUESTION_WRITER);
        }
        this.deleted(Boolean.TRUE);
        return saveDeleteHistories(loginUser);
    }

    private void deleted(boolean deleted) {
        this.deleted = deleted;
    }

    private DeleteHistories saveDeleteHistories(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = DeleteHistories.empty();
        deleteHistories.addDeleteHistory(DeleteHistory.ofQuestion(this.id, loginUser));
        deleteHistories.addAll(this.answers.deleteAll(loginUser));
        return deleteHistories;
    }

    public Long getId() {
        return this.id;
    }

    public User getWriter() {
        return this.writer;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(this.id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + this.id +
                ", title='" + this.title + '\'' +
                ", contents='" + this.contents + '\'' +
                ", writer=" + this.writer +
                ", deleted=" + this.deleted +
                '}';
    }
}
