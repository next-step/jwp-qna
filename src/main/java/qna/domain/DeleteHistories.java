package qna.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories(DeleteHistory... deleteHistories) {
        this.deleteHistories = Arrays.stream(deleteHistories)
            .collect(Collectors.toList());
    }

    public boolean addAll(DeleteHistories deleteHistories) {
        return this.deleteHistories.addAll(deleteHistories.deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteHistories)) {
            return false;
        }
        DeleteHistories that = (DeleteHistories)o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeleteHistories{");
        sb.append("deleteHistories=").append(deleteHistories);
        sb.append('}');
        return sb.toString();
    }
}
