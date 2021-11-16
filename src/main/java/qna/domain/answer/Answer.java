package qna.domain.answer;

import qna.domain.BaseTimeEntity;
import qna.domain.question.Question;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Embedded
    private Contents contents;

    private boolean deleted = false;

    protected Answer() {
    }

    private Answer(Question question, User writer, String contents) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException("사용자 정보를 확인할 수 없습니다.");
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException("질문 글을 찾을 수 없습니다.");
        }

        this.writer = writer;
        this.question = question;
        this.contents = new Contents(contents);
    }

    public static Answer create(Question question, User writer, String contents) {
        return new Answer(question, writer, contents);
    }

    public void changeDeleteState(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        this.deleted = true;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public boolean isDeleted() {
        return deleted;
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
}
