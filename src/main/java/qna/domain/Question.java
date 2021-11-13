package qna.domain;

import java.util.ArrayList;
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
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;
    
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
    
    public void deleteAnswer(Answer answer) {
        answers.delete(answer);
    }
    
    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        if (!loginUser.equals(writer)) {
            throw new CannotDeleteException("본인의 질문만 삭제 가능합니다");
        }
        if (answers.getAnswers().size() != 0 && !answers.checkWriter(writer)) {
            throw new CannotDeleteException("질문에 다른사람의 답변이 있어서 삭제 불가능");
        }
        this.deleted = true;
        List<DeleteHistory> deleteHistories = new ArrayList<DeleteHistory>();
        deleteHistories.add(DeleteHistory.of(ContentType.QUESTION, id, writer));
        answers.delete();
        return DeleteHistories.of(deleteHistories);
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

    public int countAnswers() {
        return answers.getAnswers().size();
    }

    @Override
    public String toString() {
        return "Question{" + "id=" + id + ", title='" + title + '\'' + ", contents='" + contents + '\'' + ", writer=" + writer + ", deleted=" + deleted + '}';
    }
}
