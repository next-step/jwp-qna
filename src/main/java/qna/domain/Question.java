package qna.domain;

import org.springframework.data.annotation.ReadOnlyProperty;
import qna.CannotDeleteException;
import qna.domain.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Question extends BaseEntity {
    public static final String DELETE_EXCEPTION_MESSAGE = "질문을 삭제할 권한이 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;
    private boolean deleted = false;

    @Column(length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @ReadOnlyProperty
    private final List<Answer> answers = new ArrayList<>();

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    protected Question() {
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
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

    public List<DeleteHistory> delete(User loginUser) {
        validOwner(loginUser);
        delete();
        return generateDeleteHistories(loginUser);
    }

    private List<DeleteHistory> generateDeleteHistories(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        DeleteHistory ofQuestion = DeleteHistory.ofQuestion(id, writer, LocalDateTime.now());
        deleteHistories.add(ofQuestion);
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    private void validOwner(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(DELETE_EXCEPTION_MESSAGE);
        }
    }

    private void delete() {
        this.deleted = true;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", title='" + title + '\'' +
                ", writer=" + writer +
                '}';
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

}
