package qna.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import qna.CannotDeleteException;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private QuestionContents contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Embedded
    private QuestionTitle title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void addAnswer(Answer answer) {
        throwOnEmptyAnswer(answer);
        throwOnAlreadyRegisteredAnswer(answer);

        answers.add(answer);
        answer.setQuestion(this);
    }

    private void throwOnEmptyAnswer(Answer answer) {
        if (answer == null) {
            throw new RuntimeException();
        }
    }

    private void throwOnAlreadyRegisteredAnswer(Answer answer) {
        if (answers.contains(answer)) {
            throw new RuntimeException();
        }
    }

    protected Question() {

    }

    private Question(
        Long id,
        QuestionContents contents,
        boolean deleted,
        QuestionTitle title,
        User writer
    ) {
        this.id = id;
        this.contents = contents;
        this.deleted = deleted;
        this.title = title;
        this.writer = writer;
    }

    public static Question of(User writer, String title, String contents) {
        return of(null, writer, title, contents);
    }

    public static Question of(Long id, User writer, String title, String contents) {
        throwOnEmptyTitle(title);

        return new Question(
            id,
            QuestionContents.of(contents),
            false,
            QuestionTitle.of(title),
            writer);
    }

    private static void throwOnEmptyTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목은 빈 값일 수 없습니다.");
        }
    }

    public List<DeleteHistory> delete(User deleter) {
        throwOnNotQuestionOwner(deleter);
        throwOnHavingAnyAnswersNotOwner();
        this.deleted = true;

        DeleteHistory questionDeleteHistory = DeleteHistory.ofQuestion(deleter, this);
        List<DeleteHistory> answerDeleteHistories = deleteAnswers(deleter);

        return Stream.of(Collections.singletonList(questionDeleteHistory), answerDeleteHistories)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private void throwOnNotQuestionOwner(User deleter) {
        if (!isOwner(deleter)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private void throwOnHavingAnyAnswersNotOwner() {
        if (hasAnyAnswersNotOwner()) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private List<DeleteHistory> deleteAnswers(User deleter) {
        return getNotDeletedAnswers().stream()
            .map(answer -> answer.delete(deleter))
            .collect(Collectors.toList());
    }

    private boolean hasAnyAnswersNotOwner() {
        return getNotDeletedAnswers().stream()
            .anyMatch(answer -> !answer.isOwner(writer));
    }

    boolean isOwner(User user) {
        return this.writer.equals(user);
    }

    public Long getId() {
        return id;
    }

    public QuestionContents getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public QuestionTitle getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Answer> getNotDeletedAnswers() {
        return answers.stream()
            .filter(answer -> !answer.isDeleted())
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Question question = (Question)obj;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
