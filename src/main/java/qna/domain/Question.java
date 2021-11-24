package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

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

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory setDeleted(boolean deleted) {
        this.deleted = deleted;
        return new DeleteHistory(ContentType.QUESTION, this.getId(), this.getWriter());
    }

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        if(! isOwner(user)){
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        if(! answers.isMyAnswer(user)){
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(this.setDeleted(true));
        deleteHistories.addAll(this.answers.delete());

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

    public Answers getAnswers() {
        return this.answers;
    }

    public User getWriter() {
        return this.writer;
    }
}
