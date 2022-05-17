package qna.question.domain;

import common.entity.BasicEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import qna.user.domain.User;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    private Long writerId;

    @Column(nullable = false)
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
