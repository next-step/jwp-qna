package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AttributeOverride(name = "createdDate", column = @Column(name = "created_at"))
@AttributeOverride(name = "modifiedDate", column = @Column(name = "updated_at"))
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

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
        this.contents = contents;
    }


    public void isOwner(User loginUser) throws CannotDeleteException {
        if (!this.writer.equals(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public void addQuestion(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
        this.question = question;
    }

    public void delete(User loginUser) throws CannotDeleteException {
        isOwner(loginUser);
        try {
            question.isOwner(loginUser);
            this.deleted = true;
        }catch (CannotDeleteException e) {
            question.deleteCancel();
            throw new CannotDeleteException("답변을 삭제할 권한이 없습니다.");
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
        return deleted;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Answer answer = (Answer) o;
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
                ", writer=" + writer +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
