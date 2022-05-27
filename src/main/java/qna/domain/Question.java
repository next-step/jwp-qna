package qna.domain;

import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static qna.domain.ExceptionMessage.CANNOT_DELETE_NOT_OWNER;
import static qna.domain.ExceptionMessage.CANNOT_DELETE_OTHERS_ANSWER;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(User writer, String title, String contents) {
        this(null, writer, title, contents);
    }

    public Question(Long id, User writer, String title, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
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
        return writer;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public List<DeleteHistory> deletedBy(User writer) throws CannotDeleteException {
        if (!this.isOwner(writer)) {
            throw new CannotDeleteException(CANNOT_DELETE_NOT_OWNER.getMessage());
        }

        if (!isAllAnswersOwnedBy(writer)) {
            throw new CannotDeleteException(CANNOT_DELETE_OTHERS_ANSWER.getMessage());
        }

        this.deleted = true;

        return getDeleteHistories(writer);
    }

    private boolean isAllAnswersOwnedBy(User writer) {
        return answers.stream()
                .allMatch(answer -> answer.isOwner(writer));
    }

    private List<DeleteHistory> getDeleteHistories(User writer) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        answers.forEach(answer -> {
            try {
                deleteHistories.add(answer.deletedBy(writer));
            } catch (CannotDeleteException e) {
                e.printStackTrace();
            }
        });
        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", answers=" + answers +
                ", deleted=" + deleted +
                '}';
    }
}
