package qna.domain;

public interface ExceptionFunction<T, R> {
    R apply(T r) throws Exception;
}
