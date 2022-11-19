package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import qna.CannotDeleteException;
import qna.ForbiddenException;
import qna.constant.ErrorCode;
import qna.repository.DeleteHistoryRepository;
import qna.service.DeleteHistoryService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    public List<Answer> getAnswers() {
        return answers.toGetListAnswer();
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

    public void delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ErrorCode.질문_삭제_권한.getErrorMessage());
        }
        if (hadOtherUsersAnswer(loginUser)) {
            throw new CannotDeleteException(ErrorCode.질문_삭제_다른사람_답변_존재.getErrorMessage());
        }
        this.deleted = true;
        DeleteHistory deleteHistory = DeleteHistory.create(ContentType.QUESTION, this.id, this.writer);
//        DeleteHistoryService deleteHistoryService = new DeleteHistoryService(new DeleteHistory());
        answers.toGetListAnswer().stream().forEach(answer -> answer.makeDeleted());
    }

    private boolean hadOtherUsersAnswer(User loginUser) {
        return !(answers.toGetListAnswer().stream().allMatch(answer -> answer.isOwner(loginUser)));
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
