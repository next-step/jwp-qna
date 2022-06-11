package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User user;
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
        this.user = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.user.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);

        if (answer.getQuestion() != this) {
            answer.setQuestion(this);
        }
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

    public User getUser() {
        return user;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    public Answers getAnswers() {
        return answers;
    }

    public void delete(User loginUser) throws CannotDeleteException {
        validateUser(loginUser);
        answers.delete(loginUser);
        setDeleted();
    }

    private void validateUser(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }
}
