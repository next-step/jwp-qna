package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writerId;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    List<Answer> answers = new ArrayList<>();

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    protected Question() {
    }

    public Question writeBy(User writer) {
        this.writerId = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getWriterId() {
        return writerId;
    }

    public void setWriterId(User writerId) {
        this.writerId = writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    private void validateUser(User loginUser) throws CannotDeleteException{
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        validateUser(loginUser);
        this.setDeleted(true);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.ofQuestion(this));

        for (Answer answer : this.answers) {
            answer.validateUser(loginUser);
            answer.setDeleted(true);
            deleteHistories.add(DeleteHistory.ofAnswer(answer));
        }

        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writerId +
                ", deleted=" + deleted +
                '}';
    }
}
