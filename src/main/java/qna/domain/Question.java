package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseEntity {
    private static final String ONLY_DELETED_STATE = "삭제된 질문만 허용합니다";
    private static final String NONE_AUTH_DELETE = "질문을 삭제할 권한이 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;
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

    public List<DeleteHistory> delete(User writer) {
        validateOwner(writer);
        this.deleted = true;
        List<DeleteHistory> deleteHistories = new ArrayList();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        deleteHistories.addAll(this.answers.deleteAllAnswer(writer));
        return deleteHistories;
    }

    private void validateOwner(User writer) {
        if (!this.writer.equals(writer)) {
            throw new RuntimeException(NONE_AUTH_DELETE);
        }
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
        answer.toQuestion(this);
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

    public int sizeOfAnswers() {
        return answers.size();
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }
}
