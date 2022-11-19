package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Title title;
    @Embedded
    private Contents contents;
    @Embedded
    private Answers answers;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    private boolean deleted = false;

    protected Question() {
    }

    public Question(Title title, Contents contents) {
        this(null, title, contents);
    }

    public Question(Long id, Title title, Contents contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers(new ArrayList<>());
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return Objects.equals(this.writer, writer);
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
    }

    public DeleteHistories delete(User user) {
        validateOwner(user);
        DeleteHistories deleteHistories = answers.delete(user);
        deleteHistories.add(createDeleteHistory(user));
        setDeleted(true);
        return deleteHistories;
    }

    private DeleteHistory createDeleteHistory(User user) {
        return DeleteHistory.ofQuestion(id, user);
    }

    public void validateOwner(User user) throws CannotDeleteException {
        if (!this.isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Answers getAnswers() {
        return answers;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", deleted=" + deleted +
                '}';
    }
}
