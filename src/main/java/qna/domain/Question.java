package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Answer> answers;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(title, contents, null);
    }

    public Question(String title, String contents, List<Answer> answers) {
        this(null, title, contents, answers);
    }

    public Question(Long id, String title, String contents, List<Answer> answers) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = answers;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.addQuestion(this);
        this.answers.add(answer);
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

    private void deleted() {
        deleted = true;
    }

    public List<Answer> getNotDeletedAnswers() {
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .collect(toList());
    }

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        if (!isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        deleted();

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.ofQuestion(id, user));
        addDeleteHistory(user, deleteHistories);

        return deleteHistories;
    }

    private void addDeleteHistory(User user, List<DeleteHistory> deleteHistories) throws CannotDeleteException {
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(user));
        }
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
