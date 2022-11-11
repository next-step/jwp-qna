package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistoryMaker {

    private DeleteHistoryMaker() {
        throw new IllegalStateException("유틸 클래스입니다.");
    }

    public static List<DeleteHistory> fromQuestion(Question question) {
        final List<DeleteHistory> deleteHistoryList = new ArrayList<>();
        deleteHistoryList.add(DeleteHistory.fromQuestion(question));
        for (Answer answer : question.getAnswers()) {
            deleteHistoryList.add(DeleteHistory.fromAnswer(answer));
        }
        return deleteHistoryList;
    }
}
