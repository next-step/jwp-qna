package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Where;
import qna.common.BaseEntity;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    @Column
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Embedded
    @Where(clause = "deleted = 'false'")
    private Answers answers = new Answers();

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

    private void validateRemovable(User user) {
        validateDeleted();
        validateWriter(user);
    }

    private void validateDeleted() {
        if (this.deleted) {
            throw new NotFoundException("이미 삭제된 질문입니다.");
        }
    }

    private void validateWriter(User user) {
        if (!this.writer.equals(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void addAnswer(Answer answer) {
        if (answer.getQuestion().getId() != this.getId()) {
            throw new IllegalArgumentException("현재 질문과 등록하려는 답변에 대한 질문이 일치하지 않습니다.");
        }
        this.answers.add(answer);
    }

    public void delete(User loginUser) {
        validateRemovable(loginUser);
        this.deleted = true;
    }

    public Answers getAnswers() {
        return this.answers;
    }

    public Long getId() {
        return id;
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

    public User getWriter() {
        return writer;
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
                ", user=" + writer +
                ", answers=" + answers +
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
        return deleted == question.deleted && Objects.equals(id, question.id) &&
                Objects.equals(title, question.title) && Objects.equals(contents, question.contents) &&
                Objects.equals(writer.getId(), question.writer.getId()) &&
                Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer.getId(), answers, deleted);
    }
}
