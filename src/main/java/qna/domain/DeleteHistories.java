package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public void addDeletedQuestion(Question question) {
        question.deleteQuestion();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
    }

    public void addDeletedAnswers(Answers answers) {
        for (Answer answer : answers.answers()) {
            answer.deleteAnswer();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
    }

    public List<DeleteHistory> deleteHistories() {
        return deleteHistories;
    }
}
