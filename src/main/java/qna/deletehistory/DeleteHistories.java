package qna.deletehistory;

import qna.answer.Answer;
import qna.domain.ContentType;
import qna.question.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories;

    private DeleteHistories(final List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public static DeleteHistories fromAnswers(final List<Answer> answers) {
        return new DeleteHistories(answers.stream()
                .map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getUser()))
                .collect(Collectors.toList()));
    }

    public void addDeleteQuestion(final Question question) {
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getUser()));
    }

    public List<DeleteHistory> getDeleteHistories() {
        return new ArrayList<>(deleteHistories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
