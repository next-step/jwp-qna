package qna.action;

import qna.NotFoundException;

import java.util.Objects;

public interface NullCheckAction {
    default void throwExceptionIsNullObject(Object object) {
        if (Objects.isNull(object)) {
            throw new NotFoundException();
        }
    }
}
