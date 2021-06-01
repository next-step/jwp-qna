package qna.domain;

public interface ExceptionFunction<T, R, E> {
    R apply(T r) throws Exception;
}
