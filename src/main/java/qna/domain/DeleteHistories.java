package qna.domain;

import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> elements;

    private DeleteHistories(List<DeleteHistory> elements) {
        this.elements = elements;
    }

    public static DeleteHistories of(List<DeleteHistory> elements) {
        return new DeleteHistories(elements);
    }

    public List<DeleteHistory> findReadOnlyElements() {
        return Collections.unmodifiableList(this.elements);
    }
}
