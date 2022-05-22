package qna.domain.collections;

import java.util.List;
import qna.domain.DeleteHistory;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
