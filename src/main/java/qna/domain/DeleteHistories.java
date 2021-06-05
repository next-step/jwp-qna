package qna.domain;

import java.util.Arrays;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(DeleteHistory... deleteHistories) {
        this(Arrays.asList(deleteHistories));
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public void addAll(DeleteHistories otherHistories) {
        this.deleteHistories.addAll(otherHistories.deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
