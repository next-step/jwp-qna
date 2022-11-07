package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Question {

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
    @Embedded
    private Answers answers;

    protected Question() {
    }

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
        this.writer = writer;
        return this;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.toQuestion(this);
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

    public Long getWriterId() {
        return writer.getId();
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void changeDeleted() {
        this.deleted = true;
    }

    public List<DeleteHistory> deleteContentsOf(final User loginUser) throws CannotDeleteException {
        validate(loginUser);
        validateAnswers(loginUser);
        return deleteContents();
    }

    private List<DeleteHistory> deleteContents() {
        return Stream.of(deleteQuestion().getDeleteHistories(), deleteAnswers().getDeleteHistories())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private DeleteHistories deleteQuestion() {
        DeleteHistories deleteHistories = new DeleteHistories();
        changeDeleted();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        return deleteHistories;
    }

    private DeleteHistories deleteAnswers() {
        return answers.deleteAll();
    }

    private void validateAnswers(final User loginUser) throws CannotDeleteException {
        if (answers.isIncludedFromOtherThan(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private void validate(final User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
