package qna.domain;

import java.util.ArrayList;
import java.util.Objects;
import javax.persistence.CascadeType;
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
import qna.CannotDeleteException;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

@Entity
public class Question extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;
    @Embedded
    private Answers answers;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers(new ArrayList<>());
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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public void removeAnswer(Answer answer) {
        answers.removeAnswer(answer);
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void validateSameUser(User user) {
        if(!isOwner(user)) {
            throw new CannotDeleteException(ErrorCode.질문_삭제_권한_없음.getErrorMessage());
        }
    }

    private void changeDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private DeleteHistory createDeleteHistory() {
        return DeleteHistory.ofQuestion(this.id, this.writer);
    }

    public DeleteHistories delete(User user) {
        validateSameUser(user);
        DeleteHistories deleteHistories = this.answers.delete(user);
        changeDeleted(true);
        deleteHistories.add(createDeleteHistory());
        return deleteHistories;
    }

    public int answersCount() {
        return answers.answersCount();
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
