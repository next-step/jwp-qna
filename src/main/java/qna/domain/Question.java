package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)

    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return this.writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public DeleteHistories delete(User loginUser) {
        validateIsOwner(loginUser);
        this.deleted = true;

        DeleteHistories deleteHistories = new DeleteHistories(Collections.singletonList(
            new DeleteHistory(ContentType.QUESTION, this.id, this.writer, LocalDateTime.now())
        ));
        Answers answers = new Answers(this.answers);
        return deleteHistories.concat(answers.delete(loginUser));
    }

    private void validateIsOwner(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Question question = (Question)o;
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
