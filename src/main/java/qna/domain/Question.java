package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers = Answers.empty();

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

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {

        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        List<DeleteHistory> result = new ArrayList<>();
        deleted = true;
        result.add(new DeleteHistory(ContentType.QUESTION, id, writer, this.getUpdatedAt()));
        result.addAll(answers.delete(loginUser));
        return result;
    }

    protected List<Answer> getAnswers() {
        return answers.getAnswerGroup();
    }

    private boolean isOwner(User writer) {
        return this.writer.matchId(writer);
    }

    void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
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
                ", writerId=" + this.writer.getId() +
                ", deleted=" + deleted +
                '}';
    }

}
