package qna.domain.question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import qna.domain.UpdatableEntity;
import qna.domain.User;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.exception.question.QuestionOwnerNotMatchedException;

@Entity
@Table
public class Question extends UpdatableEntity {

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "clob")
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private final List<Answer> answers = new ArrayList<>();

    @Transient
    private final AnswerList answerList = new AnswerList(answers);

    private boolean deleted = false;

    protected Question() {}

    public Question(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public User writer() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public AnswerList deleteBy(User loginUser) throws
            QuestionOwnerNotMatchedException,
            AnswerOwnerNotMatchedException {
        if (!this.isOwner(loginUser)) {
            throw new QuestionOwnerNotMatchedException();
        }
        delete();
        return answerList.deleteAllBy(loginUser);
    }

    public Answer addAnswer(User answerUser, String content) {
        Answer answer = new Answer(answerUser, this, content);
        this.answers.add(answer);
        return answer;
    }

    private void delete() {
        this.deleted = true;
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
