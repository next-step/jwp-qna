package qna.domain.entity.common;

public interface Deleteable<H> {
    void deleted();

    H deleteHistory();
}
