package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(ContentType question, Long id, User writer) {
        deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(question, id, writer));
    }

    public List<DeleteHistory> getDeleteHistories() {
        return new ArrayList<>(deleteHistories);
    }

    public List<DeleteHistory> add(List<DeleteHistory> deleteAnswerHistories) {
        deleteHistories.addAll(deleteAnswerHistories);
        return new ArrayList<>(deleteHistories);
    }
}
