package qna.domain;

import java.util.Collections;
import java.util.List;

public final class DeleteHistorys {

    private final List<DeleteHistory> list;

    private DeleteHistorys(List<DeleteHistory> deleteHistorys) {
        this.list = deleteHistorys;
    }

    public static DeleteHistorys of(List<DeleteHistory> deleteHistorys) {
        return new DeleteHistorys(deleteHistorys);
    }

    public List<DeleteHistory> list() {
        return Collections.unmodifiableList(list);
    }

    public int totalCount() {
        return list.size();
    }

}
