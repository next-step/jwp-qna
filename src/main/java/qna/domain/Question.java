package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import qna.domain.common.BaseTime;
import qna.exception.CannotDeleteException;
import qna.exception.ErrorMessages;
import qna.exception.UnAuthorizedException;

@Entity
public class Question extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private AnswerList answerList = new AnswerList();

    protected Question() {
    }

    public Question(String title, String contents, User writer) {
        this.title = title;
        this.contents = contents;
        writeBy(writer);
    }

    /**
     * 연관관계 설정 Question -> User
     *
     * @param writer
     * @return
     */
    public Question writeBy(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User target) {
        return this.writer.equalsNameAndEmail(target);
    }

    /**
     * 연관관계 설정 answer -> question
     *
     * @param answer
     */
    public void addAnswer(Answer answer) {
        if (!this.answerList.contains(answer)) {
            this.answerList.add(answer);
            answer.toQuestion(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public DeleteHistoryList deleteQuestion(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ErrorMessages.OTHER_USER_CANNOT_DELETE);
        }
        setDeleted(true);
        return getDeleteHistoryList(loginUser);
    }

    private DeleteHistoryList getDeleteHistoryList(User loginUser) throws CannotDeleteException {
        DeleteHistoryList deleteHistoryList = new DeleteHistoryList(
            new DeleteHistory(ContentType.QUESTION, id, writer));
        answerList.deleteAnswers(loginUser, deleteHistoryList);
        return deleteHistoryList;
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
        return Objects.equals(id, question.id) && Objects.equals(writer, question.getWriter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted, answerList);
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
