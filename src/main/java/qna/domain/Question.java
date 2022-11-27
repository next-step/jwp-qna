package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;
    @Embedded
    private Answers answers = new Answers();

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
        return this.writer.getId().equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
        //answer.toQuestion(this);
    }

    public void clearAnswers() {
        answers.clear();
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
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<DeleteHistory> delete(User writer) throws CannotDeleteException {
        List<DeleteHistory> deleteHistoryList = new ArrayList<>();
        deleteQuestion(writer, deleteHistoryList);
        deleteAnswerList(writer, deleteHistoryList);

        return deleteHistoryList;
    }

    private void deleteQuestion(User writer, List<DeleteHistory> deleteHistoryList) throws CannotDeleteException {
        validateOwner(writer);
        deleted();
        deleteHistoryList.add(DeleteHistory.ofQuestion(id, writer));
    }

    private void validateOwner(User writer) throws CannotDeleteException {
        if (!isOwner(writer)) {
            throw new CannotDeleteException("작성자가 아닌 경우 질문을 삭제할 수 없습니다.");
        }
    }

    private void deleteAnswerList(User writer, List<DeleteHistory> deleteHistoryList) throws CannotDeleteException {
        deleteHistoryList.addAll(answers.deleteAnswers(writer));
    }

    public void deleted() {
        deleted = true;
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
