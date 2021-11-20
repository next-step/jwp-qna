package qna.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistorys {

    private final List<DeleteHistory> deleteHistorys;

    public DeleteHistorys(List<DeleteHistory> deleteHistorys) {
        this.deleteHistorys = Collections.unmodifiableList(deleteHistorys);
    }

    public List<DeleteHistory> values() {
        return deleteHistorys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeleteHistorys that = (DeleteHistorys)o;
        return Objects.equals(deleteHistorys, that.deleteHistorys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistorys);
    }
}
