package qna.domain;

import qna.CannotDeleteException;
import qna.domain.common.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", length = 100, nullable = false)
    private String title;

    @Lob
    @Column(name="contents")
    private String contents;

    @Embedded
    private Answers answers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(name="deleted", nullable = false)
    private boolean deleted = false;

    public Question(String title, String contents, User writer) {

        this(null, title, contents, writer);
    }

    public Question(Long id, String title, String contents, User writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.answers = new Answers();
    }

    protected Question() {
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public Answers getAnswers() {
        return answers;
    }

    public void setAnswers(Answers answers) {
        this.answers = answers;
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
                ", answers=" + answers +
                ", writer=" + writer +
                ", deleted=" + deleted +
                '}';
    }

    public List<DeleteHistory> deleteQuestion(User writer) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        validateQuestion(writer);
        if( this.getAnswers().existAnswer() ) {
            deleteHistories.addAll(this.answers.deleteAnswers(writer));
        }
        this.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.id, this.writer, LocalDateTime.now()));
        return deleteHistories;
    }

    private void validateQuestion(User writer) {
        if (!this.writer.isOwner(writer)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    void removeAnswer(Answer answer) {
        this.answers.getAnswers().remove(answer);
    }
}
