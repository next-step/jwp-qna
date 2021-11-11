package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents")
    @Lob
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void addAnswer(Answer answer) {
        throwOnEmptyAnswer(answer);
        throwOnAlreadyRegisteredAnswer(answer);

        answers.add(answer);
        answer.setQuestion(this);
    }

    private void throwOnEmptyAnswer(Answer answer) {
        if (answer == null) {
            throw new RuntimeException();
        }
    }

    private void throwOnAlreadyRegisteredAnswer(Answer answer) {
        if (answers.contains(answer)) {
            throw new RuntimeException();
        }
    }

    protected Question() {

    }

    private Question(Long id, String contents, boolean deleted, String title, User writer, List<Answer> answers) {
        this.id = id;
        this.contents = contents;
        this.deleted = deleted;
        this.title = title;
        this.writer = writer;
        this.answers = answers;
    }

    public static Question of(User writer, String title, String contents) {
        return of(null, writer, title, contents);
    }

    public static Question of(Long id, User writer, String title, String contents) {
        throwOnEmptyTitle(title);

        return new Question(id, contents, false, title, writer, new ArrayList<>());
    }

    private static void throwOnEmptyTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목은 빈 값일 수 없습니다.");
        }
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isOwner(User user) {
        return this.writer.equals(user);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Question question = (Question)obj;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
