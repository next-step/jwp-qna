package qna.domain;

import qna.CannotDeleteException;
import qna.domain.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Question extends BaseEntity {
    public static final String DELETE_EXCEPTION_MESSAGE = "질문을 삭제할 권한이 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Contents contents;

    @Embedded
    private Deleted deleted;

    @Embedded
    private Title title;

    @Embedded
    private Answers answers;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.answers = new Answers();
        this.deleted = new Deleted();
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Deleted isDeleted() {
        return deleted;
    }

    public DeleteHistories delete(User loginUser) {
        validateOwner(loginUser);
        delete();
        return generateDeleteHistories(loginUser);
    }

    private DeleteHistories generateDeleteHistories(User loginUser) {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(id, writer, LocalDateTime.now()));
        deleteHistories.addAll(answers.deleteAll(loginUser));
        return deleteHistories;
    }

    private void validateOwner(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(DELETE_EXCEPTION_MESSAGE);
        }
    }

    private void delete() {
        deleted.delete();
    }

    public Answers getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents=" + contents +
                ", deleted=" + deleted +
                ", title=" + title +
                ", writer=" + writer +
                ", answers=" + answers +
                '}';
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

}
