package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "contents")
    @Lob
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    @ManyToOne
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

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

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer.toString() +
                ", deleted=" + deleted +
                '}';
    }

    public void deleteBy(User loginUser) throws CannotDeleteException {
        validateUser(loginUser);
        validateAnswersWriter(loginUser);
    }

    private void validateAnswersWriter(User loginUser) throws CannotDeleteException {
        /* 차후수정 : if 인덴트 향상 + answer에 책임주는 걸 고려할 것*/
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }
    }

    private void validateUser(User loginUser) throws CannotDeleteException {
        if (!this.writer.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }
}
