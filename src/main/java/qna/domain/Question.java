package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

@Entity
public class Question extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
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

    public Question writeBy(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void validateSameUser(User user) {
        if(!isOwner(user)) {
            throw new CannotDeleteException(ErrorCode.질문_삭제_권한_없음.getErrorMessage());
        }
    }

    private DeleteHistory changeDeleted(boolean deleted) {
        this.deleted = deleted;
        return DeleteHistory.createDeleteHistory(ContentType.QUESTION, this.id, this.writer);
    }

    public List<DeleteHistory> delete(User user) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        validateSameUser(user);
        for(Answer answer: this.answers) {
            answer.validateSameUser(user);
        }
        deleteHistories.add(changeDeleted(true));
        for(Answer answer: this.answers) {
            deleteHistories.add(answer.delete(user));
        }
        return deleteHistories;
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
