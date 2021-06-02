package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.*;

import qna.exceptions.UnAuthorizedException;
import qna.validators.StringValidator;

@Table
@Entity
public class Question extends BaseEntity {

    private static final int TITLE_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = TITLE_LENGTH, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        StringValidator.validate(title, TITLE_LENGTH);

        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
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

    public List<Answer> getAnswers(boolean deleted) {
        return answers.stream().filter(answer -> answer.isDeleted() == deleted).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Question{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", contents='" + contents + '\''
            + ", writerId=" + writer.getId()
            + ", deleted=" + deleted
            + ", createdAt=" + getCreatedAt()
            + ", updatedAt=" + getUpdatedAt()
            + '}';
    }
}
