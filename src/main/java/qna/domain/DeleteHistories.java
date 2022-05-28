package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private final List<DeleteHistory> elements = new ArrayList<>();

    public List<DeleteHistory> addHistory(Question question) {
        elements.add(question.delete());

        for (Answer answer : question.getAnswers()) {
            elements.add(answer.delete());
        }
        return elements();
    }

    private List<DeleteHistory> elements() {
        return new ArrayList<>(elements);
    }
}
