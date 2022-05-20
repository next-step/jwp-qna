package qna.domain;

import java.util.ArrayList;
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

    public static DeleteHistories empty() {
        return new DeleteHistories(new ArrayList<>());
    }

    public List<DeleteHistory> findReadOnlyElements() {
        return Collections.unmodifiableList(this.elements);
    }

    public void add(DeleteHistory deleteHistory) {
        this.elements.add(deleteHistory);
    }

    public void merge(DeleteHistories deleteHistories) {
        this.elements.addAll(deleteHistories.findReadOnlyElements());
    }
}
