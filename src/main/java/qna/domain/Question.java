package qna.domain;

import qna.CannotDeleteException;
import qna.constant.ErrorCode;

import javax.persistence.*;


@Entity
public class Question extends BaseDateEntity{

    // test용
    public static Question create(User writer) {
        return new Question("title", "contents", writer);
    }
    public static Question create(String title, String contents) {
        return new Question(null, title, contents);
    }
    public static Question create(String title, String contents, User writer) {
        return new Question(null, title, contents, writer);
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private boolean deleted = false;
    @Embedded
    private Contents contents;
    @Embedded
    private Title title;
    @Embedded
    Answers answers = new Answers();
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "WRITER_ID")
    private User writer;

    protected Question() {}

    private Question(String title, String contents) {
        this(null, title, contents);
    }

    private Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    private Question(Long id, String title, String contents) {
        this.id = id;
        this.title = Title.of(title);
        this.contents = Contents.of(contents);
    }

    public Question(Long id, String title, String contents, User writer) {
        this.id = id;
        this.title = Title.of(title);
        this.contents = Contents.of(contents);
        this.writer = writer;
    }

    public Answers getAnswers() {
        return answers;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public Long getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Contents getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public DeleteHistories delete(User loginUser){
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ErrorCode.질문_삭제_권한.getErrorMessage());
        }
        this.deleted = true;
        DeleteHistories deleteHistories = new DeleteHistories(DeleteHistory.create(ContentType.QUESTION, this.id, this.writer));
        deleteHistories.addDeleteHistory(answers.makeDeleted(loginUser));
        return deleteHistories;
    }

    public void removeAnswer(Answer answer) {
        this.answers.removeAnswer(answer);
    }

    public void addAnswer(Answer answer) {
        this.answers.addAnswer(answer);
    }

    public int hadNumberOfAnswers() {
        return this.answers.NumberOfAnswer();
    }
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title.toString() + '\'' +
                ", contents='" + contents.toString() + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }

}
