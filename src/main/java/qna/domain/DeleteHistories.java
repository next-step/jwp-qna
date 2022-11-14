package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories = new ArrayList<>();

    public void add(DeleteHistory ofQuestion) {
        this.deleteHistories.add(ofQuestion);
    }

    public void addAll(List<DeleteHistory> deleteHistories) {
        this.deleteHistories.addAll(deleteHistories);
    }

    public int size() {
        return this.deleteHistories.size();
    }

    public List<DeleteHistory> getDeleteHistories() {
        return this.deleteHistories;
    }
}
