package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        deleteHistories = new ArrayList<>();
    }

    public List<DeleteHistory> delete(Answers answers, DeleteHistory questionHistory) {
        deleteHistories.add(questionHistory);
        answers.delete(deleteHistories);
        return deleteHistories;
    }
}
