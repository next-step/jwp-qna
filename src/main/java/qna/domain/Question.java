package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Question extends BaseEntity {

    public static final String IS_NOT_OWNER = "질문을 삭제할 권한이 없습니다.";
    @Embedded
    private final Answers answers = new Answers();
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String title;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

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

    public boolean isNotOwner(User writer) {
        return !this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.updateQuestion(this);
        this.answers.addAnswer(answer);
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

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        validateDelete(loginUser);
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        deleted = true;
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        deleteHistories.addAll(answers.deleteAll(loginUser));

        return deleteHistories;
    }

    private void validateDelete(User loginUser) throws CannotDeleteException {
        if (isNotOwner(loginUser)) {
            throw new CannotDeleteException(IS_NOT_OWNER);
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", answers=" + answers +
                ", title='" + title + '\'' +
                ", writer=" + writer +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
