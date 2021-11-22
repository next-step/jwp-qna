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

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
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

    public void mappingToQuestion(Question question) {
        if (Objects.nonNull(this.question)) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        if(!question.getAnswers().contains(this)) {
            question.getAnswers().add(this);
        }
    }

    public Question getQuestion() {
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
        return this.question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getWriter() {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
        return writer;
    }

    public void mappingToWriter(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void updateAnswerContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
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

    public DeleteHistory deleteBy(User loginUser) {
        if (!isSameUser(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        this.deleted = true;
        return DeleteHistory.deleteAnswer(id, loginUser, LocalDateTime.now());
    }

    private boolean isSameUser(User loginUser) {
        return writer.equals(loginUser);
    }
}
