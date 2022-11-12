package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> histories;

    public DeleteHistories(DeleteHistory questionDeleteHistory,
                           List<DeleteHistory> answerDeleteHistories) {
        List<DeleteHistory> histories = new ArrayList<>();
        histories.add(questionDeleteHistory);
        histories.addAll(answerDeleteHistories);
        this.histories = histories;
    }

    public List<DeleteHistory> getHistories() {
        return histories;
    }
}
