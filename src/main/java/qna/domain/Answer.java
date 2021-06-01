package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.base.BaseEntity;
import qna.domain.wrapper.Deleted;

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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "answer")
@Where(clause = "deleted=0")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Embedded
    private Deleted deleted;

    protected Answer() {}

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;
        this.writer = Optional.ofNullable(writer).orElseThrow(UnAuthorizedException::new);
        this.question = Optional.ofNullable(question).orElseThrow(NotFoundException::new);
        this.contents = contents;
        this.deleted = new Deleted();
    }

    public boolean isOwner(User loginUser) {
        return this.writer.equals(loginUser);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public DeleteHistory deleteAndReturnDeleteHistory(User loginUser) {
        checkPossibleDelete(loginUser);
        setDeleted(true);
        return new DeleteHistory(ContentType.ANSWER, id, writer, LocalDateTime.now());
    }

    private void checkPossibleDelete(User loginUser) {
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

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setDeleted(boolean deleted) {
        this.deleted.setDeleted(deleted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
