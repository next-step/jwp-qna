package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    private DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = Collections.unmodifiableList(deleteHistories);
    }
    
    public static DeleteHistories of(DeleteHistory questionHistory, DeleteHistories answerHistories) {
        List<DeleteHistory> deleteHistories = new ArrayList<DeleteHistory>();
        deleteHistories.add(questionHistory);
        deleteHistories.addAll(answerHistories.getDeleteHistories());
        return new DeleteHistories(deleteHistories);
    }
    
    public static DeleteHistories fromAnswers(List<DeleteHistory> deleteHistories) {
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
    
    public int countDeleteHistories() {
        return deleteHistories.size();
    }
}
