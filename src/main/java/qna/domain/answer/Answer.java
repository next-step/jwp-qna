package qna.domain.answer;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.BaseTimeEntity;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.user.User;
import qna.domain.vo.Contents;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@SQLDelete(sql = "UPDATE Answer SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(indexes = {
        @Index(name = "answer_deleted_index", columnList = "deleted"),
})
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Column(name = "question_id", insertable = false, updatable = false)
    private Long questionId;

    @Embedded
    private Contents contents;

    private boolean deleted = Boolean.FALSE;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        validate(writer, question);
        this.id = id;
        this.writer = writer;
        this.question = question;
        this.contents = Contents.of(contents);
    }

    private void validate(User writer, Question question) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    public Answer(User writer, Question question, String contents, boolean deleted) {
        this(writer, question, contents);
        this.deleted = deleted;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        if (this.question != null) {
            this.getQuestion().removeAnswer(this);
        }
        this.question = question;
        question.getAnswers().add(this);
    }

    private DeleteHistory delete() {
        this.deleted = true;
        return DeleteHistory.ofAnswer(this.id, this.writer);
    }

    public void userClear() {
        this.writer = null;
    }

    public void updateWriter(User target) {
        this.writer.update(target);
    }

    public boolean matchContent(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }
        return this.contents.equals(Contents.of(content));
    }

    public DeleteHistory deleteByUser(User loginUser) {
        validateByUser(loginUser);
        return delete();
    }

    private void validateByUser(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents.getValue();
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        return id != null ? id.equals(answer.id) : answer.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + (Objects.isNull(writer) ? null : writer.getId()) +
                ", questionId=" + (Objects.isNull(question) ? null : question.getId()) +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
