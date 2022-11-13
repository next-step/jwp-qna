package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "question")
public class Question extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "title", length = 100, nullable = false))
    private Title title;

    @Embedded
    @AttributeOverride(name = "content", column = @Column(name = "contents"))
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;
    @Embedded
    @AttributeOverride(name = "deleted", column = @Column(name = "deleted", nullable = false))
    private DeleteFlag deleted = DeleteFlag.notDeleted();

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private Set<Answer> answers = new HashSet<>();

    public Question(Title title, Contents contents) {
        this(null, title, contents);
    }

    public Question(Long id, Title title, Contents contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question(Long id, Title title, Contents contents, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        setCreatedAt(createdAt);
    }

    protected Question() {
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Contents getContents() {
        return contents;
    }

    public void updateContents(Contents contents) {
        this.contents = contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted.getDeleted();
    }

    public void setDeleted(DeleteFlag deleted) {
        this.deleted = deleted;
    }

    public void softDeleteBy(User loginUser) throws CannotDeleteException {
        checkDeleteableOwner(loginUser);
        checkAnswersOwner();
        if (!answers.isEmpty()) {
            answers.forEach(answer -> answer.deleteStatus(DeleteFlag.deleted()));
        }
        deleted = DeleteFlag.deleted();
    }

    public void updateAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer.getUserId() +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private void checkDeleteableOwner(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private void checkAnswersOwner() throws CannotDeleteException {
        boolean isAllOwner = answers.stream()
                .filter(answer -> !answer.isDeleted())
                .allMatch(answer -> answer.isOwner(writer));
        if (!isAllOwner) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
