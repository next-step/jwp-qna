package qna.domain;

import qna.CannotDeleteException;

@FunctionalInterface
public interface FunctionalException <T, R, X extends CannotDeleteException> {
    R map(T input) throws X;
}
