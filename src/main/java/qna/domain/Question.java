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
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @Embedded
    private Answers answers = new Answers();

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

    /* 2. getAnswers()를 해도 DB에서 데이터를 가져오는 건 아닌데 */
    public Answers getAnswers() {
        return this.answers;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    /* 1.  https://wonit.tistory.com/466
    : "외래 키를 갖는 쪽에서만 UPDATE와 INSERT를 수행하고,
    * 없는 쪽은 SELECT만 수행할 것
    * → 하지만 순수자바객체 입장에서는 Question의 Answers에도 answer를 추가해주어야 한다.
    */
    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
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

    public List<DeleteHistory> deleteBy(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        validateUser(loginUser);
        this.deleted = true;
        deleteHistories.add(DeleteHistory.ofQuestion(this.id, this.writer));
        deleteHistories.addAll(validateAnswersWriter(loginUser));

        return deleteHistories;
    }

    private List<DeleteHistory> validateAnswersWriter(User loginUser) throws CannotDeleteException {
        return answers.deleteAllBy(loginUser);
    }

    private void validateUser(User loginUser) throws CannotDeleteException {
        if (!this.writer.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }
}
