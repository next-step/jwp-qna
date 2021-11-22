package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"), name = "writer_id")
    private User writer;

    @Embedded
    private Deleted deleted = Deleted.FALSE;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    public Question(String title, Contents contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, Contents contents) {
        this.id = id;
        this.title = Title.of(title);
        this.contents = contents;
    }

    public Question(String title, String contents) {
        this(null, title, Contents.of(contents));
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
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

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        deleted = Deleted.TRUE;
        DeleteHistories questionDeleteHistories = new DeleteHistories(DeleteHistory.of(ContentType.QUESTION, this.id, writer));

        DeleteHistories answerDeleteHistories = answers.delete(loginUser);
        return questionDeleteHistories.merge(answerDeleteHistories);

    }

    public Answers getAnswers() {
        return answers;
    }
}