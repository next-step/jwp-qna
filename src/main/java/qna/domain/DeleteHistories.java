package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public void addHistory(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void addHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories.addAll(deleteHistories);
    }

    public void addHistories(DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.deleteHistories);
    }

    public List<DeleteHistory> histories(){
        return deleteHistories;
    }

    public static DeleteHistories of(Question question){
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addHistory(DeleteHistory.of(question));
        deleteHistories.addHistories(DeleteHistories.of(question.answers()));
        return deleteHistories;
    }
    public static DeleteHistories of(Answers answers){
        DeleteHistories deleteHistories = new DeleteHistories();
        for(Answer answer: answers.list()){
            deleteHistories.addHistory(DeleteHistory.of(answer));
        }
        return deleteHistories;
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
