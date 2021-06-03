package qna.domain;

import static qna.domain.ContentType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    private static final String HAS_NOT_DELETE_PERMISSION_MESSAGE = "질문을 삭제할 권한이 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Question() {
    }

    public Question(final String title, final String contents, final User writer) {
        this(null, title, contents, writer);
    }

    public Question(final Long id, final String title, final String contents, final User writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public boolean isOwner(final User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(final Answer answer) {
        answers.add(answer);
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

    public Long getWriterId() {
        return writer.getId();
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void deleted() {
        this.deleted = true;
    }

    public int countOfAnswer() {
        return answers.size();
    }

    public DeleteHistories delete(final User user) throws CannotDeleteException {
        final DeleteHistories deleteHistories = new DeleteHistories(histories(validUser(user)));
        deleted();

        return deleteHistories;
    }

    private User validUser(final User writer) throws CannotDeleteException {
        return Optional.ofNullable(writer)
            .filter(this::isOwner)
            .orElseThrow(() -> new CannotDeleteException(HAS_NOT_DELETE_PERMISSION_MESSAGE));
    }

    private List<DeleteHistory> histories(final User user) throws CannotDeleteException {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(deleteHistory());
        deleteHistories.addAll(answerDeleteHistories(user));

        return deleteHistories;
    }

    private DeleteHistory deleteHistory() {
        return new DeleteHistory(QUESTION, id, writer, LocalDateTime.now());
    }

    private List<DeleteHistory> answerDeleteHistories(final User user)
        throws CannotDeleteException {

        final List<DeleteHistory> deleteHistories = new ArrayList<>();

        for (final Answer answer : answers) {
            deleteHistories.add(answer.delete(user));
        }

        return deleteHistories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Question question = (Question)o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(title, question.title)
            && Objects.equals(contents, question.contents) && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted);
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
}
