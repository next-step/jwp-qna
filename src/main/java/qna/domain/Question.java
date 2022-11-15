package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import static qna.domain.ContentType.QUESTION;

@Entity
public class  Question extends TimeEntity {
    private static final String CANT_DELETE_QUESTION = "질문을 삭제할 권한이 없습니다.";
    private static final String CANT_DELETE_OTHER_PERSON = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;
    @Column(nullable = false, columnDefinition = "bit")
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

    protected Question() {

    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (deleted) {
            throw new NotFoundException();
        }
        answer.toQuestion(this);
        answers.addAnswer(answer);
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

    public Long getWriterId() {
        if (Objects.isNull(writer)) {
            return null;
        }
        return writer.getId();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer.toString() +
                ", deleted=" + deleted +
                '}';
    }

    public int getAnswersCount() {
        return this.answers.size();
    }

    public void deleteAnswer(Answer deletedAnswer) {
        answers.deleteAnswer(deletedAnswer);
        answers.refreshAnswerWithoutDelete();
    }

    public User getWriter() {
        return this.writer;
    }

    public DeleteHistory deleteAndGetHistory() {
        setDeleted(true);
        return new DeleteHistory(QUESTION, getId(), getWriter(), LocalDateTime.now());
    }

    private boolean isAllOwnerAnswers(User owner) {
        return this.answers.allOwner(owner);
    }

    public List<DeleteHistory> deleteAnswersAndGetHistory() {
        return this.answers.allDeleteAndGetHistory();
    }

    public void validateDelete(User owner) throws CannotDeleteException {
        if (!isOwner(owner)) {
            throw new CannotDeleteException(CANT_DELETE_QUESTION);
        }
        if (!isAllOwnerAnswers(owner)) {
            throw new CannotDeleteException(CANT_DELETE_OTHER_PERSON);
        }
    }
}
