package qna.domain;

import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 답변.
 */
@Entity
public class Answer {
    /**
     * 답변 식별자.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 작성자 식별자.
     */
    private Long writerId;

    /**
     * 질문글 식별자.
     */
    private Long questionId;

    /**
     * 본문.
     */
    @Lob
    private String contents;
    /**
     * 생성시간.
     */
    @NotNull
    private LocalDateTime createAt = LocalDateTime.now();

    /**
     * 수정시간.
     */
    private LocalDateTime updatedAt;


    /**
     * 삭제된 답변이라면 true, 삭제되지 않았다면 false.
     */
    @NotNull
    private boolean deleted = false;

    protected Answer() {
        this(new User(), new Question(), null);
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writerId = writer.getId();
        this.questionId = question.getId();
        this.contents = contents;
    }

    /**
     * 작성자이면 true를 리턴하고, 아니라면 false를 리턴한다.
     *
     * @param writer 작성자 정보
     * @return 작성자 여부
     */
    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    /**
     * 질문글 식별자를 등록한다.
     */
    public void toQuestion(Question question) {
        this.questionId = question.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writerId +
                ", questionId=" + questionId +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
