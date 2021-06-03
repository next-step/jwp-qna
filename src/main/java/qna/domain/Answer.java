package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    @ManyToOne
    private Question question;

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    @ManyToOne
    private User writer;

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

    public void toQuestion(Question question) {
        /* 할당된 question 객체가 있다면
         * 기존 question의 Answer에서 this를 제거해준다.
         *  ( ← Answer의 question을 변경해줄 때 필요한 로직)
         */
        if (this.question != null) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;

        /* 연관관계의 주인인 Answer에서
         * Question에 Answer를 더해준다
         * */
        question.getAnswers().add(this);
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.toString() +
                ", question=" + question.toString() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    public DeleteHistory deleteBy(User loginUser) throws CannotDeleteException {
        if (!this.writer.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.deleted = true;
        return DeleteHistory.ofAnswer(this.id, this.writer);
    }
}
