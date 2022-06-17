package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import qna.CannotDeleteException;

public class Answers {

    List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    public boolean isContainsAnotherAnswerWriter(User writer) {
        return answers.stream()
            .map(it -> writer.equals(it.getWriter()))
            .collect(Collectors.toSet())
            .contains(false);
    }
}
