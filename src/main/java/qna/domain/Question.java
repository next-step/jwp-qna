package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import qna.CannotDeleteException;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {

    private static final String CHECK_AUTHORITY = "질문을 삭제할 권한이 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    protected Question() {}

    private Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public static Question of(String title, String contents) {
        return new Question(null, title, contents);
    }

    public static Question of(Long id, String title, String contents) {
        return new Question(id, title, contents);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public Long getId() {
        return id;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            this.answers.add(answer);
            answer.toQuestion(this);
        }
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public DeleteHistorys deleteByOwner(User loginUser) {
        validateAuthority(loginUser);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        if (null != answers) {
            deleteHistories.addAll(deleteAllAnswerByOwner(loginUser));
        }

        this.deleted = true;
        deleteHistories.add(DeleteHistory.of(ContentType.QUESTION, this.id, this.writer));

        return DeleteHistorys.of(deleteHistories);
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public DeleteHistory createDeleteHistory() {
        return DeleteHistory.of(ContentType.QUESTION, this.id, writer);
    }

    private void validateAuthority(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(CHECK_AUTHORITY);
        }
    }

    private List<DeleteHistory> deleteAllAnswerByOwner(User loginUser) {
        return answers.stream().map(answer -> answer.deleteByOwner(loginUser)).collect(Collectors.toList());
    }

}
