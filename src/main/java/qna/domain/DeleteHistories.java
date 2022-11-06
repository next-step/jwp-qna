package qna.domain;

import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> values;

    private DeleteHistories(List<DeleteHistory> values) {
        this.values = values;
    }

    public static DeleteHistories from(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getHistories() {
        return Collections.unmodifiableList(values);
    }
}
