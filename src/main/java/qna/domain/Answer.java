package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {
    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    protected Answer() {
    }

    private Answer(Long id, User writer, Question question, String contents) {
        super(id);
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public static Answer of(User writer, Question question, String contents) {
        return of(null, writer, question, contents);
    }

    public static Answer of(Long id, User writer, Question question, String contents) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        Answer answer = new Answer(id, writer, question, contents);
        question.addAnswer(answer);
        return answer;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public Question getQuestion() {
        return question;
    }

    public User getWriter() {
        return writer;
    }

    // TODO Question 변경 시 삭제
    public Long getWriterId() {
        return writer.getId();
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean hasSameQuestion(Question question) {
        return this.question == question;
    }
}
