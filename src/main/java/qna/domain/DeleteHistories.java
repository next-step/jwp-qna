package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> elements;

    public DeleteHistories() {
        elements = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> elements) {
        this.elements = elements;
    }

    public DeleteHistories(DeleteHistory questionDeleteHistory, DeleteHistories answersDeleteHistories) {
        this();
        add(questionDeleteHistory);
        addAll(answersDeleteHistories);
    }

    public void add(DeleteHistory deleteHistory) {
        this.elements.add(deleteHistory);
    }

    public void addAll(DeleteHistories deleteHistories) {
        this.elements.addAll(deleteHistories.elements());
    }

    public List<DeleteHistory> elements() {
        return elements;
    }
}
