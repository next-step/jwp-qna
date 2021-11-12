package qna.domain;

import qna.ForbiddenException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "writer_id")
    private Long writerId;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super(id);
        validateTitle(title);
        this.title = title;
        this.contents = contents;
    }

    private void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title을 입력하세요");
        }
    }

    public Question writeBy(User writer) {
        this.writerId = writer.getId();
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        // 질문이 반드시 있은 다음에 답변이 생성된다.
        // 답변에 이 질문이 포함되어 있음이 강제 되어야 한다.
        if (!answer.hasSameQuestion(this)) {
            throw new ForbiddenException("이 질문과 다른 답변을 등록할 수 없습니다");
        }
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Long getWriterId() {
        return writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted && Objects.equals(contents, question.contents) && Objects.equals(title, question.title) && Objects.equals(writerId, question.writerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, deleted, title, writerId);
    }
}
