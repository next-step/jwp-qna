package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<DeleteHistory>();
    }

    public DeleteHistories add(List<DeleteHistory> deleteHistories) {
        this.deleteHistories.addAll(deleteHistories);
        return this;
    }

    public DeleteHistories add(ContentType contentType, Long contentId, User deletedBy) {
        deleteHistories.add(DeleteHistory.of(contentType, contentId, deletedBy));
        return this;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
