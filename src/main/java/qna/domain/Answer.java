package qna.domain;

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

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {

    private static final String HAS_ANOTHER_WRITER_MESSAGE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(final User writer, final Question question, final String contents) {
        this(null, writer, question, contents);
    }

    public Answer(final Long id, final User writer, final Question question, final String contents) {
        this.id = id;
        this.contents = contents;
        this.writer = validWriter(writer);
        this.question = validQuestion(question);
    }

    private User validWriter(final User writer) {
        return Optional.ofNullable(writer)
            .orElseThrow(UnAuthorizedException::new);
    }

    private Question validQuestion(final Question question) {
        return Optional.ofNullable(question)
            .orElseThrow(NotFoundException::new);
    }

    public boolean isOwner(final User writer) {
        return this.writer.equals(writer);
    }

    public Long getId() {
        return id;
    }

    public Long getQuestionId() {
        return question.getId();
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void deleted() {
        this.deleted = true;
    }

    public DeleteHistory delete(final User user) throws CannotDeleteException {
        final User writer = validUser(user);
        deleted();

        return DeleteHistory.ofAnswer(id, writer);
    }

    private User validUser(final User writer) throws CannotDeleteException {
        return Optional.ofNullable(writer)
            .filter(this::isOwner)
            .orElseThrow(() -> new CannotDeleteException(HAS_ANOTHER_WRITER_MESSAGE));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Answer answer = (Answer)o;
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(writer,
            answer.writer) && Objects.equals(question, answer.question) && Objects.equals(contents,
            answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", writer=" + writer +
            ", question=" + question +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }
}
