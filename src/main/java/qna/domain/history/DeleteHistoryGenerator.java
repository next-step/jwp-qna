package qna.domain.history;

import qna.domain.User;
import qna.domain.content.ContentType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistoryGenerator {

    public static List<DeleteHistory> combine(DeleteHistory question, List<DeleteHistory> answer) {
        return Stream.of(Arrays.asList(question), answer)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static DeleteHistory generate(ContentType contentType, Long contentId, User writer) {
        return new DeleteHistory(contentType, contentId, writer);
    }

}
