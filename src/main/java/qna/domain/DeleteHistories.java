package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<DeleteHistory>();
    }

    public DeleteHistories add(DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.getDeleteHistories());
        return this;
    }
    
    public DeleteHistories add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
        return this;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
    
    public int countDeleteHistories() {
        return deleteHistories.size();
    }
}
