package qna.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeleteHistories implements Iterable<DeleteHistory> {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.create(question));

        for (Answer answer: question.getAnswers()) {
            deleteHistories.add(DeleteHistory.create(answer));
        }

        this.deleteHistories = deleteHistories;
    }

    @Override
    public Iterator<DeleteHistory> iterator() {
        return deleteHistories.iterator();
    }
}
