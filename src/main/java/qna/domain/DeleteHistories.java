package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    private DeleteHistories(Question question, List<Answer> answers) {
        deleteQuestion(question);
        deleteAnswers(answers);
    }

    public static DeleteHistories createDeletedHistories(Question question, List<Answer> answers) {
        return new DeleteHistories(question, answers);
    }

    private void deleteQuestion(Question question) {
        question.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
    }

    private void deleteAnswers(List<Answer> answers) {
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeleteHistories that = (DeleteHistories) o;

        return deleteHistories != null ? deleteHistories.equals(that.deleteHistories) : that.deleteHistories == null;
    }

    @Override
    public int hashCode() {
        return deleteHistories != null ? deleteHistories.hashCode() : 0;
    }
}
