package qna.domain;

import qna.exception.BlankValidateException;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseDateTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents)
            throws UnAuthorizedException, NotFoundException, BlankValidateException {

        this.id = id;
        this.writer = writer;
        toQuestion(question);
        this.contents = contents;
    }

    private static void validate(User writer, Question question, String contents)
            throws UnAuthorizedException, NotFoundException, BlankValidateException {

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        if (Objects.isNull(contents) || contents.isEmpty()) {
            throw new BlankValidateException("contents", contents);
        }
    }

    public static Answer createAnswer(User writer, Question question, String contents)
            throws UnAuthorizedException, NotFoundException, BlankValidateException {

        validate(writer, question, contents);
        return new Answer(writer, question, contents);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        if (!question.getAnswers().contains(this)) {
            this.question = question;
            question.getAnswers().add(this);
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

    public void setQuestion(Question question) {
        this.question = question;
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

    public void delete(User deleter) throws CannotDeleteException {
        validate(deleter);
        setDeleted(true);
    }

    private void validate(User deleter) {
        if (!isOwner(deleter)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        if(!isSameWriter()) {
            throw new CannotDeleteException("질문자와 답변자가 다른 경우 답변을 삭제할 수 없습니다.");
        }
    }

    private boolean isSameWriter() {
        return question.getWriter().equals(this.getWriter());
    }
}
