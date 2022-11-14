package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    List<DeleteHistory> deleteHistories = new ArrayList<>();

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public List<DeleteHistory> getDeletedHistories() {
        return deleteHistories;
    }

}
