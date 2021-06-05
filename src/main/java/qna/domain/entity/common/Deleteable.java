package qna.domain.entity.common;

import lombok.Getter;
import qna.CannotDeleteException;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
public abstract class Deleteable<T, R> extends TraceDate {

    @Column(nullable = false)
    private boolean deleted;

    public abstract boolean isOwner(T user);

    protected abstract R appendDeleteHistory(T deleter) throws CannotDeleteException;

    public final boolean nonOwner(T user) {
        return isOwner(user) == false;
    }

    public boolean isDeleted() {
        return deleted;
    }

    protected void validation(T deleter) throws CannotDeleteException {
        if (isDeleted()) {
            throw new CannotDeleteException("해당 글은 이미 삭제처리 되었습니다.");
        }

        if (nonOwner(deleter)) {
            throw new CannotDeleteException("글을 삭제할 권한이 없습니다.");
        }
    }

    public final R deleted(T deleter) throws CannotDeleteException {
        validation(deleter);

        this.deleted = true;

        return appendDeleteHistory(deleter);
    }
}
