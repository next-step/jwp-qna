package qna.domain;

import java.util.ArrayList;
import java.util.List;

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

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    Answers answers;

    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    public Question writeBy(User writer) {
        setWriter(writer);
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
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

    public User getWriter() {
        return writer;
    }

    private void setWriter(User writer) {
        if (this.writer != null) {
            this.writer.removeQuestion(this);
        }
        this.writer = writer;
        if (!writer.isWriteQuestion(this)) {
            writer.addQuestion(this);
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void addAnswer(Answer answer) {
        this.answers.addAnswer(answer);
        if (answer.getQuestion() != this) {
            answer.setQuestion(this);
        }
    }

    public void removeAnswer(Answer answer) {
        this.answers.removeAnswer(answer);
    }

    public boolean hasAnswer(Answer answer) {
        return this.answers.hasAnswer(answer);
    }

    public Answers getAnswers() {
        return answers;
    }

    public List<DeleteHistory> deleteAll(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(delete(loginUser));
        deleteHistories.addAll(this.answers.delete(loginUser));
        return deleteHistories;
    }

    public DeleteHistory delete(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.deleted = true;
        return new DeleteHistory(ContentType.QUESTION, this.id, this.writer, this.getUpdatedAt());
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
