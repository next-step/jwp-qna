package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Question extends CreatedUpdatedDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded Answers answers = new Answers();

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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (Objects.nonNull(answer)) {
            answer.toQuestion(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setContents(String contents) {
        this.contents = contents;
    }


    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public DeleteHistories remove(final User loginUser) throws CannotDeleteException {
        if (this.isDeleted()) {
            throw new CannotDeleteException("질문을 삭제 되어 있는 상태입니다.");
        }
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        DeleteHistories deleteHistories = removeNoDeletedAnswer(loginUser);
        deleteHistories.add(removeQuestion(loginUser));
        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return answers.getAnswers();
    }

    public Answers getAnswers1() {
        return answers;
    }

    private DeleteHistory removeQuestion(final User loginUser) {
        this.setDeleted(true);
        return new DeleteHistory(ContentType.QUESTION,getId(), loginUser, LocalDateTime.now());
    }

    private DeleteHistories removeNoDeletedAnswer(final User loginUser) throws CannotDeleteException {
        return answers.findAnswerBy(DeletedType.NO).remove(loginUser);
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
