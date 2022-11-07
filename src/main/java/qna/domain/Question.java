package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;
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

    public List<DeleteHistory> delete(User writer) throws CannotDeleteException {
        validateOwner(writer);
        return getDeleteHistories();
    }

    private void validateOwner(User writer) throws CannotDeleteException {
        if (!this.writer.equals(writer)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        answers.validateOwner(writer);
    }

    private DeleteHistory getDeleteHistory() {
        this.deleted = true;
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
    }

    private List<DeleteHistory> getDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList();
        deleteHistories.add(getDeleteHistory());
        deleteHistories.addAll(answers.getDeleteHistories());
        return deleteHistories;
    }

    public void addAnswer(Answer answer) {
        answers.addAnswer(answer);
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

    public int sizeOfAnswers() {
        return answers.size();
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }
}
