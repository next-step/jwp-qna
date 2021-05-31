package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Question extends BaseTimeEntity {

    public static final String CANNOT_DELETE_MESSAGE = "질문을 삭제할 권한이 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted;

    protected Question() {}

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

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public String getContents() {
        return contents;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

    public DeleteHistory deleteByOwner(User loginUser) throws CannotDeleteException {
        if (!Objects.equals(this.writer, loginUser)) {
            throw new CannotDeleteException(CANNOT_DELETE_MESSAGE);
        }
        deleted = true;

        for (Answer answer : answers) {
            answer.deleteByOwner(loginUser);
        }

        return DeleteHistory.questionOf(id, writer);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
