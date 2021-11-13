package qna.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : qna.domain
 * fileName : DeleteHistories
 * author : haedoang
 * date : 2021/11/13
 * description : DeleteHistory 일급 컬렉션 적용
 */
public class DeleteHistories {
    private final List<DeleteHistory> deleteHistoryList;

    private DeleteHistories() {
        this.deleteHistoryList = new ArrayList<>();
    }

    public static DeleteHistories of() {
        return new DeleteHistories();
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        this.deleteHistoryList.add(deleteHistory);
    }

    public List<DeleteHistory> getDeleteHistoryList() {
        return deleteHistoryList;
    }
}
