package qna.domain.qna;

import java.util.Collections;
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

import qna.common.exception.CannotDeleteException;
import qna.common.exception.InvalidParamException;
import qna.common.exception.UnAuthorizedException;
import qna.domain.BaseEntity;
import qna.domain.deleteHistory.ContentType;
import qna.domain.deleteHistory.DeleteHistory;
import qna.domain.user.User;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Lob
    @Column(name = "contents")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"), nullable = false)
    private User writer;

    @Embedded
    private final Answers answers = new Answers();

    @Column(name = "deleted", nullable = false)
    private boolean deleted = Boolean.FALSE;

    protected Question() {
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        validCanWritten(writer);

        this.writer = writer;
        return this;
    }

    public List<DeleteHistory> delete(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ContentType.QUESTION);
        }

        List<DeleteHistory> deleteHistories = answers.delete(loginUser);
        deleteHistories.add(DeleteHistory.OfQuestion(this, loginUser));

        this.deleted = true;
        return deleteHistories;
    }

    public boolean isOwner(User writer) {
        return this.writer.isMe(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers.getAnswers());
    }

    private void validCanWritten(User writer) {
        if (Objects.isNull(writer)) {
            throw new InvalidParamException();
        }

        if (writer.isGuestUser()) {
            throw new UnAuthorizedException(UnAuthorizedException.GUEST_USER_NOT_QUESTION);
        }
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", writerId=" + writer.getId() +
            ", deleted=" + deleted +
            '}';
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
        return Objects.hash(id, title, contents, writer, answers, deleted);
    }
}
