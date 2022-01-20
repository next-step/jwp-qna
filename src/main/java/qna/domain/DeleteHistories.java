package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories(Question question, List<Answer> answers) {
        setDeleteHistories(question, answers);
    }

    private void setDeleteHistories(Question question, List<Answer> answers) {
        addQuestionDeleteHistory(question);
        answers.forEach(this::addAnswerDeleteHistory);
    }

    private void addQuestionDeleteHistory(Question question) {
        deleteHistories.add(
            new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(),
                LocalDateTime.now()));
    }

    private void addAnswerDeleteHistory(Answer answer) {
        deleteHistories.add(
            new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(),
                LocalDateTime.now()));
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }
}
