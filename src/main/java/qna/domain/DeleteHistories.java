package qna.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DeleteHistories implements Iterable<DeleteHistory> {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.question(question));
        for (Answer answer : question.getAnswers()) {
            deleteHistories.add(DeleteHistory.answer(answer));
        }
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    @Override
    public Iterator<DeleteHistory> iterator() {
        return this.deleteHistories.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteHistories)) {
            return false;
        }
        DeleteHistories that = (DeleteHistories) o;
        return deleteHistories.size() == that.deleteHistories.size() &&
                deleteHistories.containsAll(that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
