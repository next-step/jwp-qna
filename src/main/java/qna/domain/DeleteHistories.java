package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> values;

    public DeleteHistories() {
        values = new ArrayList<>();
    }

    private DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.values = deleteHistories;
    }

    public static DeleteHistories ofQuestion(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()));

        return new DeleteHistories(deleteHistories);
    }

    public void addAll(List<DeleteHistory> deleteHistories) {
        values.addAll(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(values);
    }
}
