package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> items;

    public DeleteHistories(DeleteHistory param) {
        this.items = Collections.singletonList(param);
    }

    public DeleteHistories(List<DeleteHistory> params) {
        this.items = params;
    }

    public static DeleteHistories merge(DeleteHistories histories,
        DeleteHistories histories2) {
        List<DeleteHistory> items = new ArrayList<>();
        items.addAll(histories.getItems());
        items.addAll(histories2.getItems());
        return new DeleteHistories(items);
    }

    public List<DeleteHistory> getItems() {
        return Collections.unmodifiableList(items);
    }
}
