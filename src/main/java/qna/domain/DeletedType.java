package qna.domain;

import java.util.Objects;

public enum DeletedType {
    NO, YES;

    public boolean isDeleted() {
        return Objects.equals(DeletedType.YES, this);
    }
}
