package qna.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Question question;

    @Lob
    private String contents;

    private boolean deleted = false;

    protected Answer() {

    }

    Answer(User writer, Question question, String contents, boolean deleted) {
        this(writer, question, contents);
        this.deleted = deleted;
    }

    public Answer(User writer, Question question, String contents) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public void delete(User loginUser) {
        verifyWriter(loginUser);
        this.deleted = true;
    }

    private void verifyWriter(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new UnAuthorizedException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
        List<Answer> answers = question.getAnswers();
        if (!answers.contains(this)) {
            answers.add(this);
        }
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.getId() +
                ", questionId=" + question.getId() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }


}
