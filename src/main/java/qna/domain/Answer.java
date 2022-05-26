package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }


    public User getWrittenBy() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void changeDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("작성자가 아니면 삭제할 수 없습니다.");
        }
        changeDeleted(true);
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
