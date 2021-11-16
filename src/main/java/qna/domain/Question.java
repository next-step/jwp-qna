package qna.domain;

import qna.CannotDeleteException;
import qna.ForbiddenException;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {
    }

    private Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    private Question(Long id, String title, String contents, User writer) {
        super(id);
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public static Question of(String title, String contents, User writer) {
        return of(null, title, contents, writer);
    }

    public static Question of(Long id, String title, String contents, User writer) {
        validateTitle(title);
        validateWriter(writer);
        return new Question(id, title, contents, writer);
    }

    private static void validateWriter(User writer) {
        if (writer == null) {
            throw new IllegalArgumentException("작성자를 입력하세요");
        }
    }

    private static void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title을 입력하세요");
        }
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        // 질문이 반드시 있은 다음에 답변이 생성된다.
        // 답변에 이 질문이 포함되어 있음이 강제 되어야 한다.
        if (!answer.hasSameQuestion(this)) {
            throw new ForbiddenException("이 질문과 다른 답변을 등록할 수 없습니다");
        }
        answers.add(answer);
    }

    public void addAnswer(User writer, String contents) {
        addAnswer(Answer.of(writer, this, contents));
    }

    public void delete(User principal) throws CannotDeleteException {
        if (!isOwner(principal)) {
            throw new CannotDeleteException("질문 작성자만 삭제할 수 있습니다");
        }
        answers.deleteAll(writer);
        this.deleted = true;
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

    public List<Answer> getAnswers() {
        return answers.getAnswers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Question question = (Question) o;
        return deleted == question.deleted && Objects.equals(contents, question.contents) && Objects.equals(title, question.title) && Objects.equals(writer, question.writer) && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contents, deleted, title, writer, answers);
    }
}
