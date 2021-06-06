package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity{

    @Column(length = 100, nullable = false)
    private String title;

    private String contents;

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    private boolean deleted = false;

    protected Question(){
        super();
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super(id);
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer != null && this.writer.getId().equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        if(!this.answers.isEmpty() && !isAllNotDeletedAnswersOwnedByWriter()){
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        this.deleted = true;
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.getId(), this.getWriter(), LocalDateTime.now()));
        for(Answer answer: this.answers){
            deleteHistories.add(answer.delete());
        }
        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    private boolean isAllNotDeletedAnswersOwnedByWriter(){
        return this.answers.stream().allMatch(answer -> answer.isOwner(this.writer) || answer.isDeleted());
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }
}
