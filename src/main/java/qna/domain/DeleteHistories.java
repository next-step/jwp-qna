package qna.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public DeleteHistories concat(DeleteHistories other) {
        return new DeleteHistories(
            Stream.concat(this.deleteHistories.stream(), other.deleteHistories.stream()).collect(Collectors.toList())
        );
    }

    public List<DeleteHistory> getList() {
        return Collections.unmodifiableList(this.deleteHistories);
    }
}
