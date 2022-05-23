package qna.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import qna.domain.DeleteHistory;
import qna.domain.Question;

public class DeletedHistoryMapper {
    public static List<DeleteHistory> from(final Question question) {
        return Stream.concat(Stream.of(new DeleteHistory(question)), question.getAnswers().stream()
                .map(DeleteHistory::new)
        ).collect(Collectors.toList());
    }
}
