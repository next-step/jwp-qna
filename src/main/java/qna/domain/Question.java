package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import qna.CannotDeleteException;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers = Answers.empty();

    protected Question() {
    }

    Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = Title.from(title);
        this.contents = Contents.from(contents);
    }

    void addAnswer(Answer answer) {
        this.answers.add(answer);
        if (answer.getQuestion() != this) {
            answer.setQuestion(this);
        }
    }

    public Answers getAnswers() {
        return answers;
    }

    Answers notDeletedAnswers() {
        return answers.notDeletedAnswers();
    }

    public DeleteHistory delete(User loginUser, LocalDateTime deleteTime) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        setDeleted(true);

        return toDeleteHistory(deleteTime);
    }

    public DeleteHistories deleteWithAnswers(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistoryList = new ArrayList<>();
        LocalDateTime deleteTime = LocalDateTime.now();
        deleteHistoryList.add(delete(loginUser, deleteTime));
        deleteHistoryList.addAll(notDeletedAnswers().deleteAll(loginUser, deleteTime));

        return DeleteHistories.from(deleteHistoryList);
    }

    private DeleteHistory toDeleteHistory(LocalDateTime deleteTime) {
        if (deleted) {
            return new DeleteHistory(ContentType.QUESTION, id, writer, deleteTime);
        }

        throw new IllegalStateException("삭제시에만 기록을 남길 수 있습니다.");
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public Long getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Contents getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
