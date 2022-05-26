package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        this.deleteHistories = new ArrayList<>();
    }

    public void addQuestion(final Question question) {
        deleteHistories.add(
                DeleteHistory.newQuestionDeleteHistory(
                        question.getId(),
                        question.getWriter(),
                        LocalDateTime.now()
                )
        );
    }

    public void addAnswers(final List<Answer> answers) {
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete());
        }
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }
}
