package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseEntity {
    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"), name = "writer_id")
    private User user;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Question() {

    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.setId(id);
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.user = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.user.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (!this.answers.contains(answer)) {
            answer.toQuestion(this);
            answers.add(answer);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                "id=" + this.getId() +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + user.getId() +
                ", deleted=" + deleted +
                '}';
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        checkAuthorityOfDelete(loginUser);
        this.setDeleted(true);
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.getId(), loginUser, LocalDateTime.now()));

        for (Answer answer : answers) {
            answer.isOtherWrite(loginUser);
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    private void checkAuthorityOfDelete(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }
}
