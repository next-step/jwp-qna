package qna.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import qna.UnAuthorizedException;

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
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    public Answer(User writer, String contents) {
        this(null, writer, contents);
    }

    public Answer(Long id, User writer, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        this.contents = contents;
    }

    public DeleteHistory delete(User loginUser) {
        validateLoginUser(loginUser);
        this.deleted = true;
        return new DeleteHistory(ContentType.ANSWER, this.id, loginUser);
    }

    private void validateLoginUser(User loginUser) {
        if(!isSameUser(loginUser)) {
            throw new UnAuthorizedException("질문자와 답변자가 다른 경우 답변을 삭제할 수 없습니다.");
        }
    }

    private boolean isSameUser(User writer) {
        return this.writer.equals(writer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return isDeleted() == answer.isDeleted()
                && Objects.equals(getId(), answer.getId())
                && Objects.equals(getContents(), answer.getContents())
                && Objects.equals(getWriter(), answer.getWriter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContents(), isDeleted(), getWriter());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

}
