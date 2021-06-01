package qna.domain.wrapper;

import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    private DeleteHistories(final List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories of(User loginUser, Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(question.deleteAndReturnDeleteHistory(loginUser));
        deleteHistories.addAll(question.deleteAnswersAndReturnDeleteHistories(loginUser));
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
