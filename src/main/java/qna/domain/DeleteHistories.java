package qna.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DeleteHistories {
    private final LinkedList<DeleteHistory> deleteHistories;

    public DeleteHistories(DeleteHistory deleteHistory) {
        this.deleteHistories = new LinkedList<>();
        this.deleteHistories.add(deleteHistory);
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = new LinkedList<>(deleteHistories);
    }

    public static DeleteHistories of(DeleteHistory deleteHistory) {
        return new DeleteHistories(deleteHistory);
    }

    public static DeleteHistories of(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.addFirst(deleteHistory);
    }

    public List<DeleteHistory> toUnmodifiedList() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
