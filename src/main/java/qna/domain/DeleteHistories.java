package qna.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = new ArrayList<>(deleteHistories);
    }

    public static DeleteHistories create(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public static DeleteHistories merge(DeleteHistories question, DeleteHistories answer) {
        List<DeleteHistory> questionHistory = question.deleteHistories;
        List<DeleteHistory> answerHistory = answer.deleteHistories;
        List<DeleteHistory> histories = Stream
                .concat(questionHistory.stream(), answerHistory.stream())
                .collect(Collectors.toList());
        return DeleteHistories.create(histories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(new ArrayList<>(deleteHistories));
    }

    public int deleteHistoryCount() {
        return deleteHistories.size();
    }
}
