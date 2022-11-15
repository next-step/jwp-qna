package qna.domain;

import qna.CannotDeleteException;
import qna.view.Messages;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {

    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Answers getAnswers() {
        return this.answers;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public DeleteHistories delete(User loginUser) {
        DeleteHistories deleteHistories = new DeleteHistories();

        checkWriter(loginUser);
        checkAnswersWriter(loginUser);

        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        deleteHistories.addAll(answers.delete(loginUser));
        this.deleted = true;

        return deleteHistories;
    }

    private void checkWriter(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(Messages.INVALID_AUTHORIZATION.getMsg());
        }
    }

    private void checkAnswersWriter(User loginUser) {
        if (!answers.isEmpty() && !answers.isIdenticalWriter(loginUser)) {
            throw new CannotDeleteException(Messages.CONTAINS_ANSWER_WRITTEN_ANOTHER_USER.getMsg());
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getWriter() {
        return this.writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }
}