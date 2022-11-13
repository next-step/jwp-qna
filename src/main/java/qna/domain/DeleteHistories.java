package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories = new ArrayList<>();

    public void add(DeleteHistory ofQuestion) {
        this.deleteHistories.add(ofQuestion);
    }

    public int size() {
        return this.deleteHistories.size();
    }
}
