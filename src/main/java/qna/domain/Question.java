package qna.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import qna.CannotDeleteException;
import qna.base.BaseTimeEntity;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Embedded
    private Answers answers;
    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents, new Answers());
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    public Question(Long id, String title, String contents, Answers answers) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = answers;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public DeleteHistories delete(User loginUser) {
        validateLoginUser(loginUser);
        final DeleteHistories deleteHistories = DeleteHistories.valueOf(
                        new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()))
                .addAll(answers.deleteAll(loginUser));
        this.deleted = true;
        return deleteHistories;
    }

    private void validateLoginUser(final User loginUser) {
        if (!isWriter(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private boolean isWriter(final User loginUser) {
        return this.writer.equals(loginUser);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Answers getAnswers() {
        return answers;
    }

    public boolean isDeleted() {
        return deleted;
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
