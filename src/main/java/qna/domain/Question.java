package qna.domain;

import java.util.ArrayList;
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
import qna.common.exception.UnAuthorizedException;

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
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    @Embedded
    private Deleted deleted = new Deleted();

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
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        List<DeleteHistory> deleteHistories = answers.delete(loginUser);
        deleteHistories.add(deleted.deleteOf(this));

        return deleteHistories;
    }

    public boolean isOwner(User writer) {
        return this.writer.isMine(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
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
        if (writer == null) {
            throw new UnAuthorizedException();
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
            ", writerId=" + writer +
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
