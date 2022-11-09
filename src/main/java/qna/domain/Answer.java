package qna.domain;

import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {

    protected Answer() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "writer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_answer_writer")
    )
    private User writer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "question_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_answer_to_question")
    )
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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return this.writer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public DeleteHistory deleteAnswer() {
        deleted = true;
        return DeleteHistory.ofAnswer(id, writer);
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
