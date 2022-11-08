package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class DeleteHistories {

    @OneToMany(mappedBy = "deletedBy", cascade = CascadeType.ALL)
    private List<DeleteHistory> deleteHistoryList;

    public DeleteHistories(List<DeleteHistory> deleteHistoryList) {
        this.deleteHistoryList = deleteHistoryList;
    }

    protected DeleteHistories() {

    }

    public static DeleteHistories of(DeleteHistory questionDeleteHistory, DeleteHistories answerDeleteHistories) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(questionDeleteHistory);
        deleteHistories.addAll(answerDeleteHistories.getList());
        return new DeleteHistories(deleteHistories);
    }


    public List<DeleteHistory> getList() {
        return Collections.unmodifiableList(deleteHistoryList);
    }

    public void update(DeleteHistory deleteHistory) {
        this.deleteHistoryList.add(deleteHistory);
    }

    public int getSize() {
        return this.deleteHistoryList.size();
    }

    public boolean contains(DeleteHistory deleteHistory) {
        return this.deleteHistoryList.contains(deleteHistory);
    }
}
