package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Table(name = "answer")
@Entity
public class Answer extends BaseEntity {
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User user;

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

        this.user = writer;
        this.question = question;
        this.contents = contents;
    }

    public void checkPossibleDelete(User loginUser) throws CannotDeleteException {
        if(!Objects.equals(this.user, loginUser))
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void toQuestion(Question question) {
        this.question = question;
        question.addAnswer(this);
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isOwner(User writer) {
        return this.user.equals(writer);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getWriter() {
        return user;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + user.getId() +
                ", questionId=" + question.getId() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
