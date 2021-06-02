package qna.domain.entity.common;

public interface Owner<U> {
    boolean isOwner(U user);

    default boolean nonOwner(U user) {
        return isOwner(user) == false;
    }
}
