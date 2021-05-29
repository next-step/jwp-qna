package qna.domain;

import qna.CannotDeleteException;

import java.util.List;

public class Answers {

    public static final String NULL_OR_EMPTY_MESSAGE = "입력값이 null 이거나 빈 객체입니다.";
    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        validate(answers);
        this.answers = answers;
    }

    public void deleteAllByOwner(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.deleteByOwner(loginUser);
        }
    }

    private void validate(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException(NULL_OR_EMPTY_MESSAGE);
        }
    }

}
