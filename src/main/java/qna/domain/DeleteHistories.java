package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> elements = new ArrayList<>();

    public void addHistory(DeleteHistory deleteHistory) {
        elements.add(deleteHistory);
    }

    public List<DeleteHistory> elements() {
        return Collections.unmodifiableList(elements);
    }
}
