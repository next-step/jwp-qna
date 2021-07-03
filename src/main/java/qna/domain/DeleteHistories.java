package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    public DeleteHistories(Question question) {
        deleteHistories = new ArrayList<>();
        addQuestionHistory(question);
    }

    public DeleteHistories(Question question, Answers answers) {
        deleteHistories = new ArrayList<>();
        addQuestionHistory(question);
        addAnswerHistories(answers);
    }

    public static DeleteHistories of(Question question, Answers answers) {
        if (answers == null) {
            return new DeleteHistories(question);
        }
        return new DeleteHistories(question, answers);
    }

    private void addQuestionHistory(Question question) {
        deleteHistories.add(DeleteHistory.fromQuestion(question));
    }

    private void addAnswerHistories(Answers answers) {
        for (Answer answer : answers.getAnswers()) {
            deleteHistories.add(DeleteHistory.fromAnswer(answer));
        }
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

}
