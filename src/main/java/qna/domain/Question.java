package qna.domain;

import qna.CannotDeleteException;
import qna.service.DeleteHistoryService;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Table(name = "question")
@Entity
public class Question extends BaseEntity {
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, length = 100)
    private String title;

    @OneToMany(mappedBy = "question")
    @Embedded
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private User user;

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
        this.user = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return Objects.equals(this.user, writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Long getWriterId() {
        return user.getId();
    }

    public List<Answer> getAnswers() {
        return answers;
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
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + user.getId() +
                ", deleted=" + deleted +
                '}';
    }

    public List<DeleteHistory> deleteQuestion(Long questionId, User loginUser) throws CannotDeleteException {

        isPossibleDelete(loginUser);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        setDeleted(true);
        deleteHistories.add(DeleteHistory.forQuestionOf(questionId, loginUser));
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(DeleteHistory.forAnswerOf(answer.getId(), loginUser));
        }
        return deleteHistories;
    }

    boolean isPossibleDelete(User loginUser) throws CannotDeleteException {
        if (!Objects.equals(user, loginUser))
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");

        for (Answer answer : answers) {
            if (!Objects.equals(answer.getWriter(), loginUser))
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        return true;
    }
}


