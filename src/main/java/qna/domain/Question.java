package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends AbstractEntity {

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @JoinColumn(name = "writer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private final Answers answers = new Answers();

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

    public List<DeleteHistory> deleteAllAndAddHistories(User loginUser) throws CannotDeleteException {
        validateOwner(loginUser);

        List<DeleteHistory> deleteHistories = new ArrayList<>();

        deleteAndAddHistory(deleteHistories);

        deleteAllAnswersAndAddHistories(deleteHistories);

        return deleteHistories;
    }

    private void validateOwner(User loginUser) throws CannotDeleteException {
        validateIsOwner(loginUser);
        validateAnswerOwner(loginUser);
    }

    private void validateAnswerOwner(User loginUser) throws CannotDeleteException {
        answers.validateOwners(loginUser);
    }

    private void validateIsOwner(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    private void deleteAndAddHistory(List<DeleteHistory> deleteHistories) {
        delete();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
    }

    private void deleteAllAnswersAndAddHistories(List<DeleteHistory> deleteHistories) {
        answers.deleteAllAndAddHistories(deleteHistories);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }


    public boolean isDeleted() {
        return deleted;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public void delete() {
        this.deleted = true;
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
