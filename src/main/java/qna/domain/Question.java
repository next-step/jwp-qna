package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "question")
@Entity
@Where(clause = "deleted = false")
public class Question extends BaseEntity{
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writerId;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Question() {
        //JPA need no-arg constructor
    }

    public Question(String contents, boolean deleted, String title) {
        this.contents = contents;
        this.deleted = deleted;
        this.title = title;
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writerId = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
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

    public User getWriterId() {
        return writerId;
    }

    public void setWriterId(User writerId) {
        this.writerId = writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        for (Answer answer : answers) {
            answer.isWrittenBySomeoneElse(loginUser);
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        this.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.getId(), this.getWriterId(), LocalDateTime.now()));
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriterId(), LocalDateTime.now()));
        }

        return deleteHistories;
    }
}
