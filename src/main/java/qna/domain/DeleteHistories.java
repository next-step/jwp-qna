package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistoryList;

    public DeleteHistories() {
        this.deleteHistoryList = new ArrayList<>();
    }

    public void add(DeleteHistory deleteHistory){
        deleteHistoryList.add(deleteHistory);
    }

    public DeleteHistory get(int index){
        return deleteHistoryList.get(index);
    }

    public int size(){
        return deleteHistoryList.size();
    }

    public void addAll(DeleteHistories deleteHistories){
        for(int i = 0; i < deleteHistories.size(); i++){
            deleteHistoryList.add(deleteHistories.get(i));
        }
    }
}
