package qna.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST})
    private List<Answer> answers = new LinkedList<>();

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

    private boolean isOwner(User writer) {
        return this.writer == writer;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
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


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<DeleteHistory> delete(User user) {
        if (!this.isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        checkAnswersIsMine();
        List<DeleteHistory> deleteHistories = makeDeleteHistories();
        executeDeleteAnswers();

        return deleteHistories;
    }

    private List<DeleteHistory> makeDeleteHistories() {
        ArrayList<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(this.makeDeleteHistory());
        deleteHistories.addAll(
            answers.stream()
                .map(Answer::makeDeleteHistory)
                .collect(Collectors.toList()));
        return deleteHistories;
    }

    private void checkAnswersIsMine() {
        for (Answer answer : answers) {
            answer.isOwner(this.writer);
        }
    }

    private void executeDeleteAnswers() {
        this.deleted = true;
        Iterator<Answer> iter = answers.iterator();
        while (iter.hasNext()) {
            iter.next().delete();
        }
    }

    public DeleteHistory makeDeleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, this.getId(), this.writer,
            LocalDateTime.now());
    }

    public List<Answer> getAnswersIsNotDelete() {
        return this.answers.stream()
            .filter(answer -> answer.isDeleted() != true)
            .collect(Collectors.toList());
    }
}
