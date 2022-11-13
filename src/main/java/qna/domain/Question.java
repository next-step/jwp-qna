package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList();

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
        answer.toQuestion(this);
        answers.add(answer);
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

    public Long getWriterId() {
        return writer.getId();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Answers getAnswers() {
        return new Answers(this.answers);
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", title='" + title + '\'' +
                ", writerId=" + writer.getId() +
                ", " + super.toString() +
                '}';
    }

    public void checkDeleteAuth(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.getAnswers().checkDeleteAuth(loginUser);
    }

    public List<DeleteHistory> deleteAndGetDeleteHistories() {
        this.setDeleted(true);
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer));
        deleteHistories.addAll(this.getAnswers().deleteAndGetDeleteHistories());
        return deleteHistories;
    }
}
