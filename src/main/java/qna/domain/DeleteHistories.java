package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> toList() {
        return new ArrayList<>(deleteHistories);
    }
}
