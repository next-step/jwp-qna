package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseEntity {
    @Embedded
    private QuestionBody requestBody;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private final Answers answers = new Answers();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    protected Question() {
    }

    public Question(String title, String contents) {
        requestBody = new QuestionBody(title, contents);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        if (isDeleted()) {
            throw new CannotDeleteException("이미 삭제한 질문입니다.");
        }

        delete();
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, super.getId(), this.writer));
        deleteHistories.addAll(answers.delete(loginUser));
        return deleteHistories;
    }

    private void delete() {
        deleted = true;
    }
}
