package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    private DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories =  Collections.unmodifiableList(deleteHistories);
    }

    public static DeleteHistories from(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public static DeleteHistories fromQuestion(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(getDeleteHistory(question));
        deleteHistories.addAll(question.getDeleteHistoriesOfAnswers());
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    private static DeleteHistory getDeleteHistory(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
