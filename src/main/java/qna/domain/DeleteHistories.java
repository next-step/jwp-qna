package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(DeleteHistory deleteHistory) {
        deleteHistories = new ArrayList<>();
        deleteHistories.add(deleteHistory);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }

    public List<DeleteHistory> add(List<DeleteHistory> deleteAnswerHistories) {
        deleteHistories.addAll(deleteAnswerHistories);
        return new ArrayList<>(deleteHistories);
    }
}
