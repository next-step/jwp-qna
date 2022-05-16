package qna.question.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import qna.user.domain.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    private Long writerId;

    private boolean deleted = false;

    @Builder
    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writerId = writer.getId();
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public long getId() {
        return this.id;
        // fixme - 제거 방법 찾기
    }

    public long getWriterId() {
        // fixme - 제거 방법 찾기
        return this.writerId;
    }

    public boolean isDeleted() {
        // fixme - 제거 방법 찾기
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        // fixme - 제거 방법 찾기
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
