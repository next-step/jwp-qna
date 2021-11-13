package qna.domain;

import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    private DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories of(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }
    
    public DeleteHistories add(ContentType contentType, Long contentId, User deletedBy) {
        deleteHistories.add(DeleteHistory.of(contentType, contentId, deletedBy));
        return this;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
