package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question) {
        this.deleteHistories = Collections.unmodifiableList(createDeleteHistories(question));
    }

    private List<DeleteHistory> createDeleteHistories(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(),
                LocalDateTime.now()));
        deleteHistories.addAll(question.getAnswers().createDeleteHistories());
        return deleteHistories;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
