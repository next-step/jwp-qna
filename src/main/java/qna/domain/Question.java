package qna.domain;

import static java.time.LocalDateTime.*;
import static javax.persistence.FetchType.*;
import static qna.domain.ContentType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.CannotDeleteException;
import qna.ErrorMessage;
import qna.UnAuthorizedException;

@Entity
@Table(name = "question")
public class Question extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private final Answers answers = new Answers();

    @Column(nullable = false)
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
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException(ErrorMessage.USER_IS_NOT_NULL);
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

    public void addAnswers(List<Answer> answers) {
        this.answers.addAnswers(answers, this);
    }

    public List<DeleteHistory> delete(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(deleteQuestion(loginUser));
        deleteHistories.addAll(deleteAnswer(loginUser));
        return Collections.unmodifiableList(deleteHistories);
    }

    public DeleteHistory deleteQuestion(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.deleted = true;
        return new DeleteHistory(QUESTION, this, loginUser, now());
    }

    public List<DeleteHistory> deleteAnswer(User loginUser) {
        return this.answers.excludeDeleteTrueAnswers().delete(loginUser);
    }

    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean equalsTitleAndContentsAndNotDeleted(Question question) {
        if (!this.equals(question))
            return false;
        return Objects.equals(title, question.title) &&
            Objects.equals(contents, question.contents) &&
            Objects.equals(deleted, question.deleted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Question question = (Question)o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
