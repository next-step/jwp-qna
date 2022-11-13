package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import qna.common.base.BaseTimeEntity;
import qna.common.exception.CannotDeleteException;

@Entity
@Table
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    private Long writerId;

    @Embedded
    private final Answers answers = new Answers();

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    private Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public static Question of(String title, String contents){
        return new Question(title, contents);
    }

    public Question writeBy(Long writerId) {
        this.writerId = writerId;
        return this;
    }

    public void isOwner(User writer) {
        if (!this.writerId.equals(writer.getId())) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.addAnswer(answer);
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

    public Long getWriterId() {
        return writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writerId +
                ", answers=" + answers +
                ", deleted=" + deleted +
                '}';
    }
}
