package qna.domain;

import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.List;

public class Answers {

    public static final String NULL_OR_EMPTY_MESSAGE = "입력값이 null 이거나 빈 객체입니다.";
    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        validate(answers);
        this.answers = answers;
    }

    public List<DeleteHistory> deleteAllByOwner(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.deleteByOwner(loginUser));
        }
        return deleteHistories;
    }

    private void validate(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException(NULL_OR_EMPTY_MESSAGE);
        }
    }

}
