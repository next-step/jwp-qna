package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Question extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_Id")
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;
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
        return this.writer.getId().equals(writer.getId());
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

    public DeleteHistories delete(User loginUser, LocalDateTime deletedTime) {
        validateWriter(loginUser);

        DeleteHistories deleteHistories = answers.delete(loginUser, deletedTime);

        this.deleted = true;
        return deleteHistories.create(new DeleteHistory(ContentType.QUESTION, id, writer, deletedTime));
    }

    private void validateWriter(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }
}
