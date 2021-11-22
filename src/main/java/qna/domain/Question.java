package qna.domain;

import java.time.LocalDateTime;
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

import qna.NoPermissionDeleteQuestionException;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private Title title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    Answers answers;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = new Title(title);
        this.contents = contents;
        this.answers = new Answers();
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public DeleteHistorys delete(User loginUser, LocalDateTime deleteAt) {
        if (!isOwner(loginUser)) {
            throw new NoPermissionDeleteQuestionException();
        }

        this.deleted = true;

        DeleteHistory questionDeleteHistory =
            new DeleteHistory(ContentType.QUESTION, this.id, this.writer, deleteAt);
        DeleteHistorys deleteHistorys = answers.delete(loginUser, deleteAt);

        return deleteHistorys.prepend(questionDeleteHistory);
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers = this.answers.append(answer);
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Question question = (Question)o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(
            contents, question.contents) && Objects.equals(title, question.title) && Objects.equals(
            writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, deleted, title, writer);
    }
}
