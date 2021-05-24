package qna.domain;

import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

/**
 * 질문.
 */
@Entity
public class Question {

    /**
     * 질문 식별자.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 제목.
     */
    @NotNull
    private String title;

    /**
     * 본문.
     */
    @Lob
    private String contents;
    /**
     * 작성자 식별자.
     */
    private Long writerId;

    /**
     * 삭제되었다면 true, 삭제되지 않았다면 false.
     */
    @NotNull
    private boolean deleted = false;

    /**
     * 생성시간.
     */
    @NotNull
    private LocalDateTime createAt = LocalDateTime.now();

    /**
     * 수정시간.
     */
    private LocalDateTime updatedAt;


    protected Question() {
        this(null, null, null);
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    /**
     * 작성자를 등록한다.
     *
     * @param writer 작성자
     * @return 작성자가 등록된 질문
     */
    public Question writeBy(User writer) {
        this.writerId = writer.getId();
        return this;
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
     * 답변을 등록한다.
     *
     * @param answer 답변
     */
    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
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
                ", writerId=" + writerId +
                ", deleted=" + deleted +
                '}';
    }
}
