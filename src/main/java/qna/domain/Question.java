package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.CannotAddException;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    private boolean deleted = false;

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

    public boolean matchId(Question question) {
        if (Objects.isNull(this.id)) {
            return false;
        }

        return this.id.equals(question.id);
    }

    public void addAnswer(Answer answer) {
        if (!answer.isFrom(this)) {
            throw new CannotAddException("이 질문에 대한 답변이 아니므로 추가할 수 없습니다.");
        }
        this.answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) {
        validateOwner(loginUser);
        deleted = true;
        return makeDeleteHistories(loginUser);
    }

    protected void validateOwner(User writer) {
        if (!this.writer.matchId(writer)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    protected List<DeleteHistory> makeDeleteHistories(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        deleteHistories.addAll(answers.delete(loginUser));
        return deleteHistories;
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

    public Answers getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Question{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", contents='").append(contents).append('\'');
        sb.append(", writer=").append(writer);
        sb.append(", answers=").append(answers);
        sb.append(", deleted=").append(deleted);
        sb.append('}');
        return sb.toString();
    }
}
