package qna.domain;

import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseContentEntity extends BaseDateTimeEntity {
    private static final boolean CONTENT_DELETED_FLAG = true;
    private static final String EXCEPTION_MESSAGE_FOR_CANNOT_DELETE = "질문을 삭제할 권한이 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    public BaseContentEntity() {
    }

    public BaseContentEntity(Long id, User writer) {
        this(id, null, false, writer);
    }

    public BaseContentEntity(Long id, String contents) {
        this.id = id;
        this.contents = contents;
    }

    public BaseContentEntity(Long id, User writer, String contents) {
        this(id, contents, false, writer);
    }

    public BaseContentEntity(Long id, String contents, boolean deleted, User writer) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.contents = contents;
        this.deleted = deleted;
        this.writer = writer;
    }

    public boolean isOwner(User writer) {
        return this.writer.equalsNameAndEmail(writer);
    }

    public BaseContentEntity writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    void validateOwnerAndSetDeleted(User loginUser) throws CannotDeleteException {
        validateSameUser(loginUser);
        setDeleted(CONTENT_DELETED_FLAG);
    }

    void validateSameUser(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException(EXCEPTION_MESSAGE_FOR_CANNOT_DELETE);
        }
    }

    void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "BaseContentEntity{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", writer=" + writer +
                '}';
    }
}
