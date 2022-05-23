package qna.question.domain;

import common.entity.BasicEntity;
import qna.question.exception.CannotDeleteException;
import qna.question.exception.NotFoundException;
import qna.user.domain.User;
import qna.user.exception.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @Column(name = "question_id")
    private Long questionId;

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
        this.contents = contents;
        question.addAnswer(this);
    }

    protected Answer() {}

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.questionId = question.getId();
    }

    public void delete(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        this.deleted = true;
    }

    public Long getId() {
        return this.id;
    }

    public User getWriter() {
        return this.writer;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public String getContents() {
        return this.contents;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer +
                ", questionId=" + questionId +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
