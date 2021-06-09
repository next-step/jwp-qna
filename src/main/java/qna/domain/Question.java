package qna.domain;

import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static javax.persistence.FetchType.LAZY;
import static qna.domain.ContentType.QUESTION;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Lob
    private String contents;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question") // (1)
    private List<Answer> answers = new ArrayList<>(); // (2)

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(Title title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, Title title, String contents) {
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
        answers.add(answer);
    }

    public void addAnswer(User writer, String contents) {
        answers.add(new Answer(writer, this, contents));
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

    public List<DeleteHistory> delete(User writer) throws CannotDeleteException {
        if (isDeleted()) {
            throw new CannotDeleteException("이미 삭제된 데이터 입니다.");
        } else if (!isOwner(writer)) {
            throw new UnAuthorizedException("답변을 삭제할 권한이 없습니다.");
        }
        List<DeleteHistory> deleteHistories = deleteAnswers(writer);
        this.deleted = true;
        deleteHistories.add(new DeleteHistory(QUESTION, this.id, this.writer));
        return deleteHistories;
    }

    private List<DeleteHistory> deleteAnswers(User writer) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        for(Answer answer : answers) {
            deleteHistories.add(answer.delete(writer));
        }

        return deleteHistories;
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
