package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Question extends BaseEntity {

    public static final QuestionDeletableChecker deletableChecker = new QuestionDeletableChecker(
            Arrays.asList(
                    new NoAnswerRule(),
                    new AllWriterIsSameRule())
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String title;
    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    protected Question() {

    }

    public Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    public Question(Long id, String title, String contents, User writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        //answer.toQuestion(this);
        this.answers.add(answer);
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

    public void markDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(title, question.title) && Objects.equals(contents, question.contents) && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        checkOwnerOrThrow(loginUser);
        boolean deletable = deletableChecker.check(loginUser, this.answers);
        if (!deletable) {
            return new ArrayList<>();
        }
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(this.toDeleteHistory());
        deleteHistories.addAll(getAnswerDeleteHistories());
        return deleteHistories;
    }

    private List<DeleteHistory> getAnswerDeleteHistories() {
        return this.answers.stream().map(Answer::toDeleteHistory).collect(Collectors.toList());
    }

    private DeleteHistory toDeleteHistory() {
        this.markDeleted(true);
        return DeleteHistory.ofQuestion(this.getId(), this.writer);
    }

    public void checkOwnerOrThrow(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }
}
