package qna.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import qna.CannotDeleteException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean deleted = false;

    @Embedded
    private Contents contents;

    protected Answer() {
    }

    public Answer(final User writer, final String contents) {
        this(null, writer, contents);
    }

    public Answer(final Long id, final User writer, final String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.contents = new Contents(contents, writer);
    }

    DeleteHistory deleteBy(final User loginUser) {
        checkAuthority(loginUser);
        this.deleted = true;

        return new DeleteHistory(ContentType.ANSWER, id, loginUser);
    }

    void checkAuthority(final User loginUser) {
        if (!contents.isWrittenBy(loginUser)) {
            throw new CannotDeleteException("답변을 삭제할 권한이 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Contents getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", writerId=" + contents.getWriter().getId() +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }
}
