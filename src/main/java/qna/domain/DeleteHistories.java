package qna.domain;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {

    private List<DeleteHistory> values = new ArrayList<>();

    protected DeleteHistories() {
    }

    public static DeleteHistories of(Question question, Answers answers) {
        return new DeleteHistories(
            Stream.of(singletonList(DeleteHistory.of(question)), valuesOf(answers))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    public DeleteHistories(List<DeleteHistory> values) {
        this.values = values;
    }

    private static List<DeleteHistory> valuesOf(Answers answers) {
        return answers.values()
            .stream()
            .map(DeleteHistory::of)
            .collect(Collectors.toList());
    }

    public List<DeleteHistory> values() {
        return unmodifiableList(values);
    }
}
