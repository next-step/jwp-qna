package qna.domain;

import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = Collections.unmodifiableList(deleteHistories);
    }

    public static DeleteHistories fromDeleteHistories(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
