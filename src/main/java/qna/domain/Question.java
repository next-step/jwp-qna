package qna.domain;

import qna.exception.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseTimeEntity {

    protected Question() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(length = 100, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "writer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_question_writer")
    )
    private User writer;

    @Column(nullable = false)
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

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
        answer.toQuestion(this);
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

    public void setContents(String contents) {
        this.contents = contents;
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


        // TODO: 질문과 관련된 답변 찾은 후 답변도 삭제(상태 변경)

        // TODO: 질문 삭제(상태 변경)
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
        // TODO: 답변자 삭제(작성자와 다를 경우 삭제 불가 리턴)
        deleteHistoryList.addAll(answers.deleteAnswers(writer));
    }

    private void deleted() {
        deleted = true;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", deleted=" + deleted +
                '}';
    }

}
