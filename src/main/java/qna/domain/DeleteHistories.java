package qna.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = Collections.unmodifiableList(deleteHistories);
    }

    public DeleteHistories(DeleteHistory... deleteHistories) {
        this.deleteHistories = Arrays.stream(deleteHistories)
                .collect(Collectors.toList());
    }

    public List<DeleteHistory> merge(DeleteHistories other) {
        return Stream.concat(deleteHistories.stream(), other.deleteHistories.stream())
                .collect(Collectors.toList());
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }
}
