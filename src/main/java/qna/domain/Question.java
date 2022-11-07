package qna.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Lob
    @Column(name = "contents")
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Embedded
    private final Answers answers = new Answers();


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

    private void delete() {
        this.deleted = true;
    }

    public DeleteHistories deleteByUser(User user) throws CannotDeleteException {
        if (!isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        DeleteHistories deleteHistories = getAnswers().deleteAll(user);

        this.delete();

        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, getId(), writer));

        return deleteHistories;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.toQuestion(this);
    }

    public void addAnswers(List<Answer> answerList) {
        answers.addAll(answerList);
    }

    public Answers getAnswers() {
        return answers;
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

    public void updateWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
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
