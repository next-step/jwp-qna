package qna.domain;

import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswers {

    private final List<Answer> answers;

    public QuestionAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    // TODO: 단위 테스트
    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            DeleteHistory answerDeleteHistory = answer.delete(loginUser);
            deleteHistories.add(answerDeleteHistory);
        }
        return deleteHistories;
    }
}
