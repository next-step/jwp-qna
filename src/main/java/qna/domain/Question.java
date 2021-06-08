package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers;


    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(Title title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, Title title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistories delete(User loginUser) {
        this.deleted = true;
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestionDeleteHistory(this.id, loginUser));
        return deleteHistories;
    }

    public DeleteHistories deleteWithAnswers(User loginUser) {
        validateOwner(loginUser);
        validateDeletable(loginUser);
        DeleteHistories questionDeleteHistories = this.delete(loginUser);
        DeleteHistories answerDeleteHistories = answers.delete(loginUser);
        return questionDeleteHistories.addAll(answerDeleteHistories);
    }

    private void validateDeletable(User loginUser) {
        if (!answers.isDeletable(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private void validateOwner(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
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
