package qna.domain.question;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.CannotDeleteException;
import qna.domain.BaseTimeEntity;
import qna.domain.answer.Answer;
import qna.domain.answer.Answers;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;
import qna.domain.vo.Contents;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE Question SET deleted = true WHERE id=?")
//@FilterDef(name = "deletedQuestionFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
//@Filter(name = "deletedQuestionFilter", condition = "deleted = :isDeleted")
@Where(clause = "deleted=false")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Embedded
    private Contents contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    private boolean deleted = Boolean.FALSE;

    @Embedded
    private final Answers answers = new Answers();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = Contents.of(contents);
    }

    public Question(String title, String contents, boolean deleted) {
        this(title, contents);
        this.deleted = deleted;
    }

    public List<DeleteHistory> deleteByUser(User loginUser) throws CannotDeleteException {
        validateDeleteByUser(loginUser);
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(delete());
        deleteHistories.addAll(answers.deleteAnswer(loginUser));
        return deleteHistories;
    }

    private DeleteHistory delete() {
        this.deleted = true;
        return DeleteHistory.ofQuestion(this.id, this.writer);
    }

    private void validateDeleteByUser(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }


    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }


    public void removeAnswer(Answer answer) {
        this.answers.remove(answer);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Answers getAnswers() {
        return answers;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Question question = (Question) o;

        return id != null ? id.equals(question.id) : question.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
