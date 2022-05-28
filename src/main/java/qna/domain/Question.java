package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import qna.exception.CannotDeleteException;
import qna.exception.UnAuthorizedException;

@Entity
@Table(name = "question")
public class Question extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User writer;

    private boolean deleted = false;

    protected Question() {

    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public List<DeleteHistory> deleteByUser(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        try{
            deleteHistories.add(this.delete(loginUser));
            deleteHistories.addAll(this.deleteAnswers(loginUser));
        } catch (UnAuthorizedException e) {
            throw new CannotDeleteException(e.getMessage(), e);
        }
        return deleteHistories;
    }

    private DeleteHistory delete(User loginUser)  {
        verifyWriter(loginUser);
        this.deleted = true;
        return DeleteHistoryFactory.createQuestionDeleteHistory(this);
    }

    private void verifyWriter(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new UnAuthorizedException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private List<DeleteHistory> deleteAnswers(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
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

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
        answer.toQuestion(this);
    }

    public User getWriter() {
        return writer;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
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
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }


}
