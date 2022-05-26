package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User user;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Answer> answers;

    protected Question() {

    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new ArrayList<>();
    }

    public Question writeBy(User user) {
        this.user = user;
        return this;
    }

    public boolean isOwner(User user) {
        return this.user.getId().equals(user.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
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
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", user=" + user +
                ", deleted=" + deleted +
                ", answers=" + answers +
                '}';
    }

    public List<DeleteHistory> deleteQuestionAndAnswer(User loginUser) {
        LocalDateTime deletedAt = LocalDateTime.now();
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        deleteHistories.add(delete(loginUser, deletedAt));
        for (Answer answer : answers)
            deleteHistories.add(answer.delete(loginUser, deletedAt));

        return deleteHistories;
    }

    private DeleteHistory delete(User loginUser, LocalDateTime deletedAt) {
        if (!isOwner(loginUser))
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");

        if (isDeleted())
            throw new CannotDeleteException("이미 삭제된 질문입니다.");

        this.deleted = true;

        return new DeleteHistory(ContentType.QUESTION, id, user, deletedAt);
    }
}
