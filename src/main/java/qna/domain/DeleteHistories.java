package qna.domain;

import static qna.domain.DeleteHistory.createAnswerDeleteHistory;
import static qna.domain.DeleteHistory.createQuestionDeleteHistory;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    private DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories of(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(createQuestionDeleteHistory(question));

        if (hasNoAnswers(question)) {
            return new DeleteHistories(deleteHistories);
        }

        deleteHistories.addAll(createAnswerDeleteHistory(question.getAnswers()));
        return new DeleteHistories(deleteHistories);
    }

    private static boolean hasNoAnswers(Question question) {
        return question.getAnswers().size() == 0;
    }

    public int getSize() {
        return deleteHistories.size();
    }

    public List<DeleteHistory> getList() {
        return deleteHistories;
    }
}
