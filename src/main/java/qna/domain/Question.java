package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();
    @Column(nullable = false)
    private boolean deleted = false;

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

    private boolean isWriter(final User loginUser) {
        return writer.equals(loginUser);
    }

    public List<DeleteHistory> delete(final User loginUser) throws CannotDeleteException {
        if (!isWriter(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.deleted = true;
        List<DeleteHistory> deleteHistories = deleteAnswers(loginUser);
        deleteHistories.add(
            new DeleteHistory(ContentType.QUESTION, id, loginUser.getId(), LocalDateTime.now()));
        return deleteHistories;
    }

    private List<DeleteHistory> deleteAnswers(User loginUser) {
        return answers.stream()
            .map(answer -> answer.delete(loginUser))
            .collect(Collectors.toList());
    }

    public void changeContent(final String contents) {
        this.contents = contents;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Long getWriterId() {
        return writer.getId();
    }
}
