package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "answer")
@Entity
@Where(clause = "deleted = false")
public class Answer extends BaseEntity {

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writerId;

    protected Answer() {
        //JPA need no-arg constructor
    }

    public Answer(String contents, boolean deleted) {
        this.contents = contents;
        this.deleted = deleted;
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {


        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writerId = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
        question.getAnswers().add(this);
    }



    public User getWriterId() {
        return writerId;
    }

    public void setWriterId(User writerId) {
        this.writerId = writerId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public void isWrittenBySomeoneElse(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
