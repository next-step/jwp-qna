package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table
public class Question extends BaseEntity {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String title;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

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
        answer.setQuestion(this);
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public List<Answer> getNotDeletedAnswers() {
        return this.answers.stream()
                .filter(a -> !a.isDeleted())
                .collect(Collectors.toList());
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", title='" + title + '\'' +
                ", writer=" + writer +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public void validateDelete(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        for (Answer answer : answers) {
            raiseIfNotAnswerOwner(loginUser, answer);
        }
    }

    private void raiseIfNotAnswerOwner(User loginUser, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public List<DeleteHistory> delete() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now()));
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete());
        }

        return deleteHistories;
    }
}
