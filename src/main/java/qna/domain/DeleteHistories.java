package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> list = new ArrayList<>();

    public void add(DeleteHistory deleteHistory) {
        list.add(deleteHistory);
    }

    public void add(DeleteHistories deleteHistories) {
        list.addAll(deleteHistories.getList());
    }

    public List<DeleteHistory> getList() {
        return Collections.unmodifiableList(list);
    }
}
