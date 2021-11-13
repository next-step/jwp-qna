package qna.domain;

import static qna.exception.ExceptionMessage.*;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import qna.exception.CannotDeleteException;

@Entity
public class Question extends BaseEntityTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @Embedded
    private Answers answers = new Answers();

    @OneToOne
    @JoinColumn(name = "write_id")
    private User writer;

    private boolean deleted = false;

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

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public void setAnswers(Answers answers) {
        this.answers = answers;
    }

    public Answers getAnswers() {
        return answers;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void validateQuestionOwner(User owner) {
        if (!isOwner(owner)) {
            throw new CannotDeleteException(CANNOT_DELETE_QUESTION_MESSAGE.getMessage());
        }
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
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

    public void delete() {
        deleted = true;
        answers.deleteAll();
    }
}
