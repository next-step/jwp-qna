package qna.domain.deletehistory;

import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    protected DeleteHistories() {

    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
    }

    public static DeleteHistories of(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> merge(List<DeleteHistory> mergeableDeleteHistories){
        deleteHistories.addAll(mergeableDeleteHistories);
        return deleteHistories;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
