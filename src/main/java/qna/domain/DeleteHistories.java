package qna.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = Collections.unmodifiableList(deleteHistories);
    }

    public List<DeleteHistory> merge(List<DeleteHistory> otherHistories) {
        return Stream.concat(deleteHistories.stream(), otherHistories.stream())
                .collect(Collectors.toList());
    }
}
