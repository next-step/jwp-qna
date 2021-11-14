package qna.deletehistory;

import qna.answer.Answer;
import qna.domain.ContentType;
import qna.question.Question;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    public DeleteHistories(final List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories fromAnswers(final List<Answer> answers) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getUser()));
        }
        return new DeleteHistories(deleteHistories);
    }

    public DeleteHistories addDeleteQuestion(Question question) {
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getUser()));
        return new DeleteHistories(deleteHistories);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return new ArrayList<>(deleteHistories);
    }
}
