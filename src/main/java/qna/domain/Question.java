package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8090430385344706191L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers = new Answers();

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question() {

    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        if (answer.getQuestion() != this) {
            answer.toQuestion(this);
        }
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        checkUser(loginUser);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        setDeleted(true);

        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, loginUser, LocalDateTime.now()));
        deleteHistories.addAll(answers.delete(loginUser));

        return deleteHistories;
    }

    private void checkUser(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public boolean containsAnswer(Answer answer) {
        return answers.contains(answer);
    }

    public Long getId() {
        return id;
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

    public Long getWriterId() {
        return writer.getId();
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Answers getAnswers() {
        return answers;
    }

    public User getWriter() {
        return writer;
    }


    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getUserId() +
                ", deleted=" + deleted +
                '}';
    }

}
