package qna.domain;

import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(name= "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private final Answers answers = new Answers();

    private static final String DELETE_ERROR = "질문을 삭제할 권한이 없습니다.";

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
        this.answers.addAnswer(answer);
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

    public User getWriter() {
        return writer;
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

    public List<DeleteHistory> delete(User deleteUser) throws CannotDeleteException {
        if (!isOwner(deleteUser)) {
            throw new CannotDeleteException(DELETE_ERROR);
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        this.deleted = true;
        deleteHistories.add(DeleteHistory.ofQuestion(this.id, this.writer));

        deleteHistories.addAll(answers.deleteAll(deleteUser));
        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer +
                ", deleted=" + deleted +
                '}';
    }
}
