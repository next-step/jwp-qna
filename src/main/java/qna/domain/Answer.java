package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.base.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity {

    public static final String ANSWERS_EXISTED = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @Embedded
    private Contents contents;

    @Embedded
    private Deletion deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
        this.writer = writer;
        this.question = question;
        this.contents = new Contents(contents);
        this.deleted = new Deletion();
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents.getContent();
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public DeleteHistory delete(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ANSWERS_EXISTED);
        }
        this.deleted.delete();
        return new DeleteHistory(ContentType.ANSWER, this.id, loginUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(getId(), answer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", question=" + question +
                '}';
    }
}
