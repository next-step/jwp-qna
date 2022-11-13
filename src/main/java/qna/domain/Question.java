package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.List;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Embedded
    private Answers answers = new Answers();
    @Column(nullable = false)
    private boolean deleted = false;

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

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getWriter() {
        return this.writer;
    }

    public List<Answer> getAnswers() {
        return this.answers.getAnswers();
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validateOwner(loginUser);
        this.deleted = true;
        return createDeleteHistories(loginUser);
    }

    private void validateOwner(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private DeleteHistories createDeleteHistories(User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(this.getId(), loginUser));
        deleteHistories.addAll(this.answers.delete(loginUser));
        return deleteHistories;
    }
}
