package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity {
    /**
     * create table answer
     * (
     * id          bigint generated by default as identity,
     * contents    clob,
     * created_at  timestamp not null,
     * deleted     boolean   not null,
     * question_id bigint,
     * updated_at  timestamp,
     * writer_id   bigint,
     * primary key (id)
     * )
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private Boolean deleted = false;

    @ManyToOne(cascade = CascadeType.ALL)
    private Question question;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

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

        this.question.addAnswer(this);
    }

    public Answer() {

    }

    public void isOwner(User user) throws CannotDeleteException {
        if (!this.writer.equals(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getQuestionId() {
        return question.getId();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory delete(User user) throws CannotDeleteException {
        isOwner(user);
        this.deleted = true;
        return new DeleteHistory(ContentType.ANSWER, this.getId(), user);
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
