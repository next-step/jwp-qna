package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contents")
    private String contents;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
    @Column(name = "title", nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;
    @Embedded
    private Answers answers;


    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    public Question() {}

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
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
                ", answers=" + answers +
                '}';
    }

    private void checkQuestionOwnerSameLoginUser(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private void checkAnswerOwnerAndLoginUser(User loginUser) throws CannotDeleteException {
        if (answers.checkWriterAndLoginUser(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        checkQuestionOwnerSameLoginUser(loginUser);
        checkAnswerOwnerAndLoginUser(loginUser);
        return deleteQuestionAnswerHistory();
    }

    private List<DeleteHistory> deleteQuestionAnswerHistory() {
        return Stream.of(deleteQuestion().getDeleteHistories(), deleteAnswers().getDeleteHistories())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private DeleteHistories deleteQuestion() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleted = true;
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        return deleteHistories;
    }

    private DeleteHistories deleteAnswers() {
        return answers.deleteAnswers();
    }
}
