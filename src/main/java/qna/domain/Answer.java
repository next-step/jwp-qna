package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_Id")
    private User writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_Id")
    private Question question;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;

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
        this.contents = contents;
    }

    protected Answer() {
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory delete(User loginUser, LocalDateTime deletedTime) {
        validateWriterUser(loginUser);
        deleted = true;
        return new DeleteHistory(ContentType.ANSWER, id, writer, deletedTime);
    }

    private void validateWriterUser(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
