package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        this.deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question));
    }

    public void addAnswer(Answer answer) {
        this.deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer));
    }

    public List<DeleteHistory> toList() {
        return this.deleteHistories;
    }
}
