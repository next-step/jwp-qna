package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    private static final String QUESTION_ERROR_MESSAGE = "질문을 삭제할 권한이 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents")
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Embedded
    private Answers answers = new Answers();

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Answers getAnswers() {
        return answers;
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
        answers.add(answer);
        answer.setQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
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

    public User getWriter() {
        return this.writer;
    }

    public DeleteHistories delete(User user, LocalDateTime localDateTime) {
        validateQuestion(user);
        deleted = true;
        return DeleteHistories.fromDeleteHistories(
                Stream.of(
                                Collections.singletonList(DeleteHistory.questionDeleteHistoryOf(id, user, localDateTime)),
                                answers.delete(user, localDateTime).getDeleteHistories()
                        ).flatMap(deleteHistories -> deleteHistories.stream())
                        .collect(Collectors.toList()));
    }

    private void validateQuestion(User user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException(QUESTION_ERROR_MESSAGE);
        }
    }
}
