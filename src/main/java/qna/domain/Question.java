package qna.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "clob")
    private String contents;

    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @Embedded
    private final QuestionAnswers answers = new QuestionAnswers();

    protected Question() {
    }

    public Question(final User writer, final String title, final String contents) {
        this(null, writer, title, contents);
    }

    public Question(final Long id, final User writer, final String title, final String contents) {
        this.id = id;
        this.contents = contents;
        this.title = Objects.requireNonNull(title);
        this.writer = writer;
    }

    public List<DeleteHistory> deleteBy(final User loginUser) {
        checkAuthority(loginUser);

        final List<DeleteHistory> deleteHistories = answers.deleteBy(loginUser);

        this.deleted = true;
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, loginUser));
        return deleteHistories;
    }

    private void checkAuthority(User loginUser) {
        if (!writer.equalsAccount(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void addAnswer(final Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public QuestionAnswers getAnswers() {
        return answers;
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
