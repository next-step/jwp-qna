package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;
    private final Answers answers;

    public DeleteHistories(Answers answers) {
        deleteHistories = new ArrayList<>();
        this.answers = answers;
    }

    public List<DeleteHistory> delete(DeleteHistory questionHistory) {
        deleteHistories.add(questionHistory);
        answers.delete(deleteHistories);
        return deleteHistories;
    }
}
